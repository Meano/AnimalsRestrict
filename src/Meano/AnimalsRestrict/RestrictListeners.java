package Meano.AnimalsRestrict;

import net.meano.Residence.Residence;
import net.meano.Residence.protection.ClaimedResidence;
import net.steeleyes.catacombs.Dungeon;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;

public class RestrictListeners implements Listener {
	public AnimalsRestrict ARM;
	int random;

	public RestrictListeners(AnimalsRestrict GetPlugin) {
		ARM = GetPlugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (event.getMessage().toLowerCase().startsWith("/sethome") || event.getMessage().toLowerCase().startsWith("/homeset")) {
			if (ARM.CatacombsPlugin.getDungeons().getDungeon(player.getLocation().getBlock()) != null)
				event.setCancelled(true);
		}else if(event.getMessage().toLowerCase().startsWith("/gs")){
			event.getPlayer().sendMessage(ChatColor.RED+"因世界分布调整，请使用/groupspawn命令，或到其他出生点。");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockDispenseEvent(BlockDispenseEvent event){
		if(	event.getItem().getType().equals(Material.MINECART)
			||event.getItem().getType().equals(Material.COMMAND_MINECART)
			||event.getItem().getType().equals(Material.EXPLOSIVE_MINECART)
			||event.getItem().getType().equals(Material.HOPPER_MINECART)
			||event.getItem().getType().equals(Material.POWERED_MINECART)
			||event.getItem().getType().equals(Material.STORAGE_MINECART)){
			//event.setCancelled(true);
		}
		
		if(event.getItem().getAmount()<=0){
			event.setItem(new ItemStack(Material.AIR));
			event.setCancelled(true);
		}
	}
	
	//玩家传送事件
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		// 传送之星和homespawn的Cause是Command只处理这部分
		if (!event.getCause().equals(TeleportCause.COMMAND))
			return;
		Location To = event.getTo();
		Player TeleportPlayer = event.getPlayer();
		Block ToBlock = To.getBlock();
		Block FromBlock = event.getFrom().getBlock();
		if (ARM.CatacombsPlugin != null) {
			if (event.getPlayer().isOp())
				return;
			Dungeon InDungeon = ARM.CatacombsPlugin.getDungeons().getDungeon(FromBlock);
			Dungeon ToDungeon = ARM.CatacombsPlugin.getDungeons().getDungeon(ToBlock);
			if (InDungeon != null) {
				TeleportPlayer.sendMessage(ChatColor.RED + "抱歉，你在迷宫地牢内，移动取消！");
				event.setTo(event.getFrom());
				event.setCancelled(true);
				return;
			}
			if (ToDungeon != null) {
				TeleportPlayer.sendMessage(ChatColor.RED + "抱歉，目的地在迷宫地牢内，移动取消！");
				event.setTo(event.getFrom());
				event.setCancelled(true);
				return;
			}
		}
		if (TeleportPlayer.isOp())
			return;
		if (Math.abs(To.getBlockX()) > 4000 || Math.abs(To.getBlockZ()) > 4000) {
			TeleportPlayer.sendMessage(ChatColor.RED + "抱歉，4000之外的地图暂未开放，将在未来开放，请耐心等待，移动取消！");
			event.setTo(event.getFrom());
			event.setCancelled(true);
			return;
		}
		ClaimedResidence ToRes = Residence.getResidenceManager().getByLoc(To);
		if (ToRes == null)
			return;
		if (ToRes.getOwner().equals(TeleportPlayer.getName()))
			return;
		if (ToRes.getPermissions().playerHas(TeleportPlayer.getName(), "build", true))
			return;
		TeleportPlayer.sendMessage(ChatColor.RED + "抱歉，你的目的地不是你的地盘，移动取消！请向领主申请build权限方可传送。");
		event.setTo(event.getFrom());
		event.setCancelled(true);
	}

	// 轨道掉落事件，用于防止刷铁轨
	@EventHandler
	public void onRailDrop(BlockPhysicsEvent event) {
		Material BlockType = event.getBlock().getType();
		if (BlockType == Material.POWERED_RAIL || BlockType == Material.DETECTOR_RAIL || BlockType == Material.ACTIVATOR_RAIL) {
			if (event.getChangedType().equals(Material.RAILS)){
				event.setCancelled(true);
			}
		}
	}

	// 打破轨道事件
	@EventHandler
	public void onBreakRail(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		Block block = event.getBlock();
		Material BlockType = block.getType();
		if (BlockType == Material.POWERED_RAIL || BlockType == Material.DETECTOR_RAIL || BlockType == Material.ACTIVATOR_RAIL) {
			switch (BlockType) {
				default:
					break;
				case POWERED_RAIL:
					block.getWorld().dropItem(block.getLocation(), ARM.PowerRail);
					break;
				case DETECTOR_RAIL:
					block.getWorld().dropItem(block.getLocation(), ARM.DetectorRail);
					break;
				case ACTIVATOR_RAIL:
					block.getWorld().dropItem(block.getLocation(), ARM.ActivatorRail);
					break;
			}
			block.setType(Material.AIR);
			event.setCancelled(true);
		}
	}

	// 放置铁轨事件
	@EventHandler(priority = EventPriority.LOW)
	public void onPlaceRail(BlockPlaceEvent event) {
		Block block = event.getBlockPlaced();
		Material BlockType = block.getType();
		if (BlockType == Material.POWERED_RAIL || BlockType == Material.DETECTOR_RAIL || BlockType == Material.ACTIVATOR_RAIL) {
			if (event.getItemInHand().getItemMeta().hasLore()) {
				block.setMetadata("PowerRailVesion", new FixedMetadataValue(ARM, 1));
			} else {
				block.setType(Material.AIR);
				event.setCancelled(true);
				event.getPlayer().getInventory().remove(event.getItemInHand());
			}
		}
	}

	// 物品合成事件
	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		Recipe CraftRecipe = event.getRecipe();
		Material CraftType = CraftRecipe.getResult().getType();
		if (CraftRecipe.getResult().equals(new ItemStack(Material.BEACON))) {
			InventoryView CraftView = event.getView();
			ItemStack Star = CraftView.getItem(5);
			if (Star.getItemMeta().hasLore()) {
				event.setCancelled(true);
				return;
			} else {
				return;
			}
		} else if (CraftType == Material.POWERED_RAIL || CraftType == Material.DETECTOR_RAIL || CraftType == Material.ACTIVATOR_RAIL) {
			if (!CraftRecipe.getResult().getItemMeta().hasLore()) {
				event.setCancelled(true);
				((Player) (event.getWhoClicked())).sendMessage(ChatColor.DARK_GREEN + "请使用新合成表合成动力探测激活轨道，详询群326355263。");
				return;
			} else {
				((Player) (event.getWhoClicked())).sendMessage(ChatColor.GREEN + "请空手破坏已放置的动力探测激活轨道，用工具或者水流等拆除会变成普通轨道被清除！");
			}
		} else if (CraftRecipe.getResult().equals(new ItemStack(Material.SLIME_BLOCK))) {
			if (((Player) (event.getWhoClicked())).hasPermission("AnimalsRestrict.SlimeBlock"))
				return;
			((Player) (event.getWhoClicked())).sendMessage(ChatColor.DARK_GREEN + "史莱姆方块还在内测中，部分玩家可以合成~");
			event.setCancelled(true);
		}
	}

	// 动物伤害事件，用于领地动物保护
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity animal = e.getEntity();
		if ((animal instanceof EnderDragon) || (animal instanceof EnderCrystal)) {
			if (damager instanceof Arrow) {
				Arrow arrow = (Arrow) damager;
				ProjectileSource shooter = arrow.getShooter();
				if (shooter instanceof Player) {
					Player player = (Player) shooter;
					if (player.hasAchievement(Achievement.THE_END)) {
						e.setCancelled(true);
						player.sendMessage("抱歉，你已经杀过龙了");
					} else
						return;
				}
			} else if (damager instanceof Player) {
				Player player = (Player) damager;
				if (player.hasAchievement(Achievement.THE_END)) {
					e.setCancelled(true);
					player.sendMessage("抱歉，你已经杀过龙了");
				} else
					return;
				e.setCancelled(true);
			}
		}
	}

	

	//领地门保护
	/*@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Material type = block.getType();
		if (type.equals(Material.ACACIA_DOOR) || type.equals(Material.ACACIA_FENCE_GATE) || type.equals(Material.BIRCH_DOOR) || type.equals(Material.BIRCH_FENCE_GATE) || type.equals(Material.DARK_OAK_DOOR) || type.equals(Material.DARK_OAK_FENCE_GATE) || type.equals(Material.JUNGLE_DOOR) || type.equals(Material.JUNGLE_FENCE_GATE) || type.equals(Material.SPRUCE_DOOR) || type.equals(Material.SPRUCE_DOOR)) {
			Location loc = block.getLocation();
			ClaimedResidence res = Residence.getResidenceManager().getByLoc(loc);
			if (res == null)
				return;
			if (res.getOwner().equals(player.getName()))
				return;
			if (res.getPermissions().playerHas(player.getName(), "use", true))
				return;
			if (player.isOp()) {
				player.sendMessage("这是玩家领地内的门，记得关好。");
				return;
			}
			player.sendMessage(ChatColor.RED + "你没有领地的使用权限！");
			event.setCancelled(true);
		}
	}*/

	public Block getEndSlimeBlock(Block FirstSlimeBlock, BlockFace SlimeBlockDirection) {
		Block EndSlimeBlock;
		EndSlimeBlock = FirstSlimeBlock;
		for (int i = 0; i < 10; i++) {
			FirstSlimeBlock = FirstSlimeBlock.getRelative(SlimeBlockDirection);
			if (FirstSlimeBlock.getType().equals(Material.SLIME_BLOCK)) {
				EndSlimeBlock = FirstSlimeBlock;
			}
		}
		return EndSlimeBlock.getRelative(SlimeBlockDirection);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent event) {
		//Bukkit.broadcastMessage("getDirectionBlock:"+event.getBlock().getRelative(event.getDirection()).getType().name());
		//for(Block b: event.getBlocks()){
		//	Bukkit.broadcastMessage("getRetractBlocks:"+b.getType().name());
		//}
		
		if (event.isCancelled())
			return;
		BlockFace RetractFace = event.getDirection().getOppositeFace();
		Block RetractBlock = event.getBlock().getRelative(RetractFace).getRelative(RetractFace);
		// 若为粘性活塞
		if (event.getBlock().getType().equals(Material.PISTON_MOVING_PIECE)&&(ARM.CatacombsPlugin.getDungeons().getDungeon(event.getBlock())==null)) {
			// 若拉动方块为史莱姆方块
			if (RetractBlock.getType().equals(Material.SLIME_BLOCK)) {
				if (ARM.CatacombsPlugin.getDungeons().getDungeon(getEndSlimeBlock(RetractBlock, RetractFace).getLocation().getBlock()) != null){
					event.setCancelled(true);
					Bukkit.broadcast("地牢拉动取消！", "AnimalsRestrict.SlimeBlock");
				}
			} else {
				if (ARM.CatacombsPlugin.getDungeons().getDungeon(getEndSlimeBlock(RetractBlock, RetractFace).getLocation().getBlock()) != null) {
					event.setCancelled(true);
					Bukkit.broadcast("地牢拉动取消！", "AnimalsRestrict.SlimeBlock");
				}
			}
		}
	}
	
	//粘液块推动保护
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent event) {
		Dungeon dungeonExtend;
		Dungeon dungeonPiston = ARM.CatacombsPlugin.getDungeons().getDungeon(event.getBlock());
		if (dungeonPiston == null) {
			for (Block b : event.getBlocks()) {
				dungeonExtend = ARM.CatacombsPlugin.getDungeons().getDungeon(b);
				if (dungeonExtend != null) {
					event.setCancelled(true);
					Bukkit.broadcast("发生从地牢外推动物品出去的事件！", "AnimalsRestrict.SlimeBlock");
					return;
				}
			}
		}
	}
}
