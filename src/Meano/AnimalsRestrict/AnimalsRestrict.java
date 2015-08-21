package Meano.AnimalsRestrict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.steeleyes.catacombs.Catacombs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AnimalsRestrict extends JavaPlugin {
	// �������
	public AnimalsRestrict ARMe = this;
	public int Version = 1;
	public RemoveAnimal ToRemoveAnimal = new RemoveAnimal();
	public ItemStack PowerRail;
	public ItemStack DetectorRail;
	public ItemStack ActivatorRail;
	public Catacombs CatacombsPlugin;

	// �����������ϳɱ�
	public void NewRecipe() {
		// ��������
		PowerRail = new ItemStack(Material.POWERED_RAIL, 1);
		ItemMeta PowerRailMeta = PowerRail.getItemMeta();
		PowerRailMeta.setLore(Arrays.asList(new String[] { ChatColor.GOLD + "���Ͷ�������", ChatColor.BLUE + "Ver1.0", ChatColor.YELLOW + "Fix by Meano" }));
		PowerRail.setItemMeta(PowerRailMeta);
		ItemStack PowerRailNew = PowerRail.clone();
		PowerRailNew.setAmount(6);
		ShapedRecipe PowerRailRecipe = new ShapedRecipe(PowerRailNew).shape(new String[] { "#*#", "#%#", "#*#" }).setIngredient('#', Material.GOLD_INGOT).setIngredient('*', Material.REDSTONE).setIngredient('%', Material.STICK);
		Bukkit.getServer().addRecipe(PowerRailRecipe);
		// ̽������
		DetectorRail = new ItemStack(Material.DETECTOR_RAIL, 1);
		ItemMeta DetectorRailMeta = DetectorRail.getItemMeta();
		DetectorRailMeta.setLore(Arrays.asList(new String[] { ChatColor.GOLD + "����̽������", ChatColor.BLUE + "Ver1.0", ChatColor.YELLOW + "Fix by Meano" }));
		DetectorRail.setItemMeta(DetectorRailMeta);
		ItemStack DetectorRailNew = DetectorRail.clone();
		DetectorRailNew.setAmount(6);
		ShapedRecipe DetectorRailRecipe = new ShapedRecipe(DetectorRailNew).shape(new String[] { "#*#", "#%#", "#*#" }).setIngredient('#', Material.IRON_INGOT).setIngredient('*', Material.REDSTONE).setIngredient('%', Material.STONE_PLATE);
		Bukkit.getServer().addRecipe(DetectorRailRecipe);
		// ��������
		ActivatorRail = new ItemStack(Material.ACTIVATOR_RAIL, 1);
		ItemMeta ActivatorRailMeta = ActivatorRail.getItemMeta();
		ActivatorRailMeta.setLore(Arrays.asList(new String[] { ChatColor.GOLD + "���ͼ�������", ChatColor.BLUE + "Ver1.0", ChatColor.YELLOW + "Fix by Meano" }));
		ActivatorRail.setItemMeta(ActivatorRailMeta);
		ItemStack ActivatorRailNew = ActivatorRail.clone();
		ActivatorRailNew.setAmount(6);
		ShapedRecipe ActivatorRailRecipe = new ShapedRecipe(ActivatorRailNew).shape(new String[] { "#*#", "#*#", "#*#" }).setIngredient('#', Material.IRON_INGOT).setIngredient('*', Material.REDSTONE_TORCH_ON);
		Bukkit.getServer().addRecipe(ActivatorRailRecipe);
	}

	public void onEnable() {
		getLogger().info("AnimalsRestrict" + Version + "���ڼ���");
		NewRecipe();
		// Residence Add-ons
		if (getServer().getPluginManager().getPlugin("Residence") != null) {
			getLogger().info("���ҵ�Residence��������ӹ��ܿ���ʹ�ã�");
		} else {
			getLogger().info("δ�ҵ�Residence��������ӹ��ܲ���ʹ�ã�");
		}
		if (getServer().getPluginManager().getPlugin("Catacombs") != null) {
			CatacombsPlugin = (Catacombs) getServer().getPluginManager().getPlugin("Catacombs");
			getLogger().info("���ҵ�Catacombs��������ӹ��ܿ���ʹ�ã�");
		} else {
			getLogger().info("δ�ҵ�Catacombs��������ӹ��ܲ���ʹ�ã�");
		}
		// ChunkLoad
		Chunk[] Chunks_world = getServer().getWorld("world").getLoadedChunks();
		Chunk[] Chunks_world_the_end = getServer().getWorld("world_the_end").getLoadedChunks();
		Chunk[] Chunks_world_nether = getServer().getWorld("world_nether").getLoadedChunks();
		getLogger().info("������������" + Chunks_world.length + "�����顣" + getServer().getWorld("world").getKeepSpawnInMemory());
		getLogger().info("ĩ������������" + Chunks_world_the_end.length + "�����顣" + getServer().getWorld("world_the_end").getKeepSpawnInMemory());
		getLogger().info("��������������" + Chunks_world_nether.length + "�����顣" + getServer().getWorld("world_nether").getKeepSpawnInMemory());
		// getServer().getWorld("world").setKeepSpawnInMemory(false);
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new RestrictListeners(this), this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				ToRemoveAnimal.Restrict();
			}
		}, 1 * 25 * 20, 23 * 60 * 21);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "say ����������5������һ��������ά�������������輸ʮ�뼴���ٴν�����Ϸ������պö�����Ҫ����~");
				Bukkit.getScheduler().scheduleSyncDelayedTask(ARMe, new Runnable() {
					@Override
					public void run() {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
					}
				}, 100L);
			}
		}, 8 * 60 * 60 * 21);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("AnimalsRestrict")) {
			if (args.length > 0) {
				if (args[0].toLowerCase().equals("any")) {
					Animals MoreRemove = new Animals();
					MoreRemove.Cow = 2;
					MoreRemove.Chicken = 2;
					MoreRemove.MushroomCow = 2;
					MoreRemove.Villager = 2;
					MoreRemove.Pig = 2;
					MoreRemove.Sheep = 4;
					MoreRemove.Wolf = 2;
					MoreRemove.Rabbit = 2;
					ToRemoveAnimal.Restrict(MoreRemove);
				}
				if (args[0].toLowerCase().equals("me")) {
					if (sender instanceof Player) {
						ToRemoveAnimal.RestrictPlayer((Player) sender);
					}
				}
				if (args[0].toLowerCase().equals("noend")) {
					if (args.length > 1) {
						Player toSetPlayer = Bukkit.getPlayer(args[1]);
						if (toSetPlayer == null) {
							sender.sendMessage("û�������ң�");
						} else {
							toSetPlayer.removeAchievement(Achievement.THE_END);
							sender.sendMessage("�Ƴ������" + toSetPlayer.getName() + "�� [�����ˡ�] �ɾͣ�");
						}
					}
				}
				if (args[0].toLowerCase().equals("end")) {
					if (args.length > 1) {
						Player toSetPlayer = Bukkit.getPlayer(args[1]);
						if (toSetPlayer == null) {
							sender.sendMessage("û�������ң�");
						} else {
							toSetPlayer.awardAchievement(Achievement.THE_END);
							sender.sendMessage("���" + toSetPlayer.getName() + "�õ��� [�����ˡ�] �ɾͣ�");
						}
					}
				}
				if (args[0].toLowerCase().equals("lc")) {
					if (sender.isOp()) {
						Chunk[] Chunks_world = getServer().getWorld("world").getLoadedChunks();
						Chunk[] Chunks_world_the_end = getServer().getWorld("world_the_end").getLoadedChunks();
						Chunk[] Chunks_world_nether = getServer().getWorld("world_nether").getLoadedChunks();
						sender.sendMessage("������������" + Chunks_world.length + "�����顣" + getServer().getWorld("world").getKeepSpawnInMemory());
						sender.sendMessage("ĩ������������" + Chunks_world_the_end.length + "�����顣" + getServer().getWorld("world_the_end").getKeepSpawnInMemory());
						sender.sendMessage("��������������" + Chunks_world_nether.length + "�����顣" + getServer().getWorld("world_nether").getKeepSpawnInMemory());
					}
				}if (args[0].toLowerCase().equals("ul")) {
					if (sender.isOp()) {
						if(sender instanceof Player){
							List<Chunk> Chunks_world = new ArrayList<Chunk>();
							int chunkx = ((Player) sender).getLocation().getChunk().getX();
							int chunkz = ((Player) sender).getLocation().getChunk().getZ();
							sender.sendMessage("���ڸ��µ������ȣ�");
							for(int xc = chunkx-5;xc<chunkx+5;xc++){
								for(int zc = chunkz-3;zc<chunkz+3;zc++){
									Chunks_world.add(getServer().getWorld("world").getChunkAt(xc, zc));
								}
							}
							for(Chunk c : Chunks_world){
								for(int x=0;x<16;x++){
									for(int z=0;z<16;z++){
										for(int y=128;y>0;y--){
											Block b = c.getBlock(x, y, z);
											if(b.getType().equals(Material.MOSSY_COBBLESTONE)){
												if(CatacombsPlugin.getDungeons().getDungeon(b)!=null){
													b.setType(Material.JACK_O_LANTERN);
												}
											}
										}
									}
								}
							}
							for(Chunk c : Chunks_world){
								for(int x=0;x<16;x++){
									for(int z=0;z<16;z++){
										for(int y=128;y>0;y--){
											Block b = c.getBlock(x, y, z);
											if(b.getType().equals(Material.JACK_O_LANTERN)){
												if(CatacombsPlugin.getDungeons().getDungeon(b)!=null){
													b.getState().update();
												}
											}
										}
									}
								}
							}
							for(Chunk c : Chunks_world){
								for(int x=0;x<16;x++){
									for(int z=0;z<16;z++){
										for(int y=128;y>0;y--){
											Block b = c.getBlock(x, y, z);
											if(b.getType().equals(Material.JACK_O_LANTERN)){
												if(CatacombsPlugin.getDungeons().getDungeon(b)!=null){
													b.setType(Material.MOSSY_COBBLESTONE);
												}
											}
										}
									}
								}
							}
							sender.sendMessage("�������ȸ�����ϣ�");
						}
					}
				}
			} else {
				ToRemoveAnimal.Restrict();
			}
			return true;
		}
		return false;
	}

	// ����������
	public class Animals {
		int Cow;
		int Pig;
		int Villager;
		int Sheep;
		int Chicken;
		int MushroomCow;
		int Wolf;
		int Rabbit;

		// ����ʱ����
		public Animals() {
			Cow = 0;
			Pig = 0;
			Villager = 0;
			Sheep = 0;
			Chicken = 0;
			MushroomCow = 0;
			Wolf = 0;
			Rabbit = 0;
		}

		// ������ж��������
		int AnimalsCount() {
			return Cow + Pig + Villager + Sheep + Chicken + MushroomCow + Wolf + Rabbit;
		}

		// ������������б��ڵĶ�������
		void AddAnimalsCount(Animals OtherAnimals) {
			Cow += OtherAnimals.Cow;
			Pig += OtherAnimals.Pig;
			Villager += OtherAnimals.Villager;
			Sheep += OtherAnimals.Sheep;
			Chicken += OtherAnimals.Chicken;
			MushroomCow += OtherAnimals.MushroomCow;
			Wolf += OtherAnimals.Wolf;
			Rabbit += OtherAnimals.Rabbit;
		}
	}

	// ����ʵ���б���
	public class AnimalsLists {
		List<Entity> Cow = new ArrayList<Entity>();
		List<Entity> Pig = new ArrayList<Entity>();
		List<Entity> Villager = new ArrayList<Entity>();
		List<Entity> Sheep = new ArrayList<Entity>();
		List<Entity> Chicken = new ArrayList<Entity>();
		List<Entity> MushroomCow = new ArrayList<Entity>();
		List<Entity> Wolf = new ArrayList<Entity>();
		List<Entity> Rabbit = new ArrayList<Entity>();
		Animals DeleteCount = new Animals();
		Animals AllDeleteCount = new Animals();

		// ���㶯��ʵ������
		public int AllAnimalsCount() {
			return Cow.size() + Pig.size() + Villager.size() + Sheep.size() + Chicken.size() + MushroomCow.size() + Wolf.size() + Rabbit.size();
		}

		public void AddAnimals(AnimalsLists OtherAnimals) {
			Cow.addAll(OtherAnimals.Cow);
			Pig.addAll(OtherAnimals.Pig);
			Villager.addAll(OtherAnimals.Villager);
			Sheep.addAll(OtherAnimals.Sheep);
			Chicken.addAll(OtherAnimals.Chicken);
			MushroomCow.addAll(OtherAnimals.MushroomCow);
			Wolf.addAll(OtherAnimals.Wolf);
			Rabbit.addAll(OtherAnimals.Rabbit);
			AllDeleteCount.AddAnimalsCount(OtherAnimals.DeleteCount);
		}
	}

	// �Ƴ�������
	public class RemoveAnimal {
		int CheckChunkD; // ���ﵥ�߼��뾶
		public Animals ThresholdCount = new Animals(); // ����ɾ����ֵ
		public Animals ThresholdCountSingle = new Animals(); // �����鶯��ɾ����ֵ

		public AnimalsLists SingleChunkCheck(Player PlayerNow, int XCycle, int ZCycle, int ChunkThresholdCount) {
			AnimalsLists SingleChunkAnimals = new AnimalsLists();
			Chunk CheckChunk = PlayerNow.getWorld().getChunkAt(XCycle, ZCycle);
			Entity Entities[] = CheckChunk.getEntities();
			for (int ECount = 0; ECount < Entities.length; ECount++) {
				switch (Entities[ECount].getType()) {
					case COW:
						SingleChunkAnimals.Cow.add(Entities[ECount]);
						break;
					case PIG:
						SingleChunkAnimals.Pig.add(Entities[ECount]);
						break;
					case VILLAGER:
						SingleChunkAnimals.Villager.add(Entities[ECount]);
						break;
					case SHEEP:
						SingleChunkAnimals.Sheep.add(Entities[ECount]);
						break;
					case CHICKEN:
						SingleChunkAnimals.Chicken.add(Entities[ECount]);
						break;
					case MUSHROOM_COW:
						SingleChunkAnimals.MushroomCow.add(Entities[ECount]);
						break;
					case WOLF:
						SingleChunkAnimals.Wolf.add(Entities[ECount]);
						break;
					case RABBIT:
						SingleChunkAnimals.Rabbit.add(Entities[ECount]);
						break;
					default:
						break;
				}
			}
			if (SingleChunkAnimals.AllAnimalsCount() > ChunkThresholdCount) {
				DeleteAnimals(SingleChunkAnimals, ThresholdCountSingle);
				getServer().broadcastMessage((new StringBuilder(PlayerNow.getName()).append("��ֳ�ܶȳ���").append(SingleChunkAnimals.DeleteCount.AnimalsCount())).toString());
			}
			return SingleChunkAnimals;
		}

		// ����ֵ�����Ƴ�����ʵ���б��ڵ�ʵ��
		public void DeleteAnimals(AnimalsLists ToRestrict, Animals DeleteThreshold) {
			Random RandRemove = new Random();
			Animals DeleteCount = ToRestrict.DeleteCount;
			if (ToRestrict.Villager.size() > DeleteThreshold.Villager) {
				DeleteCount.Villager = ToRestrict.Villager.size() - DeleteThreshold.Villager;
				for (int i = 0; i < DeleteCount.Villager; i++) {
					((Entity) ToRestrict.Villager.remove(RandRemove.nextInt(ToRestrict.Villager.size()))).remove();
				}
			}
			if (ToRestrict.Cow.size() > DeleteThreshold.Cow) {
				DeleteCount.Cow = ToRestrict.Cow.size() - DeleteThreshold.Cow;
				for (int i = 0; i < DeleteCount.Cow; i++) {
					((Entity) ToRestrict.Cow.remove(RandRemove.nextInt(ToRestrict.Cow.size()))).remove();
				}
			}
			if (ToRestrict.Pig.size() > DeleteThreshold.Pig) {
				DeleteCount.Pig = ToRestrict.Pig.size() - DeleteThreshold.Pig;
				for (int i = 0; i < DeleteCount.Pig; i++) {
					((Entity) ToRestrict.Pig.remove(RandRemove.nextInt(ToRestrict.Pig.size()))).remove();
				}
			}
			if (ToRestrict.Sheep.size() > DeleteThreshold.Sheep) {
				DeleteCount.Sheep = ToRestrict.Sheep.size() - DeleteThreshold.Sheep;
				for (int i = 0; i < DeleteCount.Sheep; i++) {
					((Entity) ToRestrict.Sheep.remove(RandRemove.nextInt(ToRestrict.Sheep.size()))).remove();
				}
			}
			if (ToRestrict.Chicken.size() > DeleteThreshold.Chicken) {
				DeleteCount.Chicken = ToRestrict.Chicken.size() - DeleteThreshold.Chicken;
				for (int i = 0; i < DeleteCount.Chicken; i++) {
					((Entity) ToRestrict.Chicken.remove(RandRemove.nextInt(ToRestrict.Chicken.size()))).remove();
				}
			}
			if (ToRestrict.MushroomCow.size() > DeleteThreshold.MushroomCow) {
				DeleteCount.MushroomCow = ToRestrict.MushroomCow.size() - DeleteThreshold.MushroomCow;
				for (int i = 0; i < DeleteCount.MushroomCow; i++) {
					((Entity) ToRestrict.MushroomCow.remove(RandRemove.nextInt(ToRestrict.MushroomCow.size()))).remove();
				}
			}
			if (ToRestrict.Wolf.size() > DeleteThreshold.Wolf) {
				DeleteCount.Wolf = ToRestrict.Wolf.size() - DeleteThreshold.Wolf;
				for (int i = 0; i < DeleteCount.Wolf; i++) {
					((Entity) ToRestrict.Wolf.remove(RandRemove.nextInt(ToRestrict.Wolf.size()))).remove();
				}
			}
			if (ToRestrict.Rabbit.size() > DeleteThreshold.Rabbit) {
				DeleteCount.Rabbit = ToRestrict.Rabbit.size() - DeleteThreshold.Rabbit;
				for (int i = 0; i < DeleteCount.Rabbit; i++) {
					((Entity) ToRestrict.Rabbit.remove(RandRemove.nextInt(ToRestrict.Rabbit.size()))).remove();
				}
			}
		}

		// ��ʼ��Ĭ����ֵ����
		public RemoveAnimal() {
			ThresholdCount.Cow = 20; // ţ������Ϊ16
			ThresholdCount.Pig = 20; // �������Ϊ16
			ThresholdCount.Villager = 20; // ���������Ϊ16
			ThresholdCount.Sheep = 20; // �������Ϊ20
			ThresholdCount.Chicken = 20; // ��������Ϊ16
			ThresholdCount.MushroomCow = 20; // Ģ��ţ������Ϊ16
			ThresholdCount.Wolf = 20; // �ǵ�����Ϊ16
			ThresholdCount.Rabbit = 20; // ���ӵ�����Ϊ16
			CheckChunkD = 3; // ������鵥����3
			ThresholdCountSingle.Cow = 2; // ţ������Ϊ2
			ThresholdCountSingle.Pig = 2; // �������Ϊ2
			ThresholdCountSingle.Villager = 2; // ���������Ϊ2
			ThresholdCountSingle.Sheep = 2; // �������Ϊ2
			ThresholdCountSingle.Chicken = 2; // ��������Ϊ2
			ThresholdCountSingle.MushroomCow = 2; // Ģ��ţ������Ϊ2
			ThresholdCountSingle.Wolf = 2; // �ǵ�����Ϊ2
			ThresholdCountSingle.Rabbit = 2; // ���ӵ�����Ϊ2
		}

		void SetThresholdCount(int Cow, int Pig, int Villager, int Sheep, int Chicken, int MushroomCow, int Wolf, int Rabbit) {
			ThresholdCount.Cow = Cow;
			ThresholdCount.Pig = Pig;
			ThresholdCount.Villager = Villager;
			ThresholdCount.Sheep = Sheep;
			ThresholdCount.Chicken = Chicken;
			ThresholdCount.MushroomCow = MushroomCow;
			ThresholdCount.Wolf = Wolf;
			ThresholdCount.Rabbit = Rabbit;
		}

		void SetThresholdCountSingle(int Cow, int Pig, int Villager, int Sheep, int Chicken, int MushroomCow, int Wolf, int Rabbit) {
			ThresholdCountSingle.Cow = Cow;
			ThresholdCountSingle.Pig = Pig;
			ThresholdCountSingle.Villager = Villager;
			ThresholdCountSingle.Sheep = Sheep;
			ThresholdCountSingle.Chicken = Chicken;
			ThresholdCountSingle.MushroomCow = MushroomCow;
			ThresholdCountSingle.Wolf = Wolf;
			ThresholdCountSingle.Rabbit = Rabbit;
		}

		public int RestrictPlayer(Player PlayerNow, int CheckChunkD, Animals ThresholdCount) {
			AnimalsLists MultiChunkAnimals = new AnimalsLists(); // �ܵĶ���
			Chunk PlayerChunk = PlayerNow.getLocation().getChunk();
			int PlayerChunkX = PlayerChunk.getX();
			int PlayerChunkZ = PlayerChunk.getZ();
			for (int XCycle = (PlayerChunkX - CheckChunkD); XCycle < (PlayerChunkX + CheckChunkD); XCycle++) {
				for (int ZCycle = (PlayerChunkZ - CheckChunkD); ZCycle < (PlayerChunkZ + CheckChunkD); ZCycle++) {
					MultiChunkAnimals.AddAnimals(SingleChunkCheck(PlayerNow, XCycle, ZCycle, 35));
				}
			}
			DeleteAnimals(MultiChunkAnimals, ThresholdCount);
			MultiChunkAnimals.AllDeleteCount.AddAnimalsCount(MultiChunkAnimals.DeleteCount);
			int Count = MultiChunkAnimals.AllDeleteCount.AnimalsCount();
			if (Count != 0) {
				getServer().broadcastMessage((new StringBuilder(PlayerNow.getName()).append("��ֳ����").append(Count)).toString());
			}
			return Count;
		}

		public int RestrictPlayer(Player PlayerNow) {
			return RestrictPlayer(PlayerNow, CheckChunkD, ThresholdCount);
		}

		public void Restrict(int CheckChunkD, Animals RestrictThresholdCount) {
			int DelAnimals = 0;
			for (Player Ponline : getServer().getOnlinePlayers()) {
				DelAnimals += RestrictPlayer(Ponline, CheckChunkD, RestrictThresholdCount);
			}
			if (DelAnimals != 0) {
				getServer().broadcastMessage((new StringBuilder(ChatColor.YELLOW + "[����]����������ÿλ�����Χһ����Χ�ڶ�����ֳ���������Զ�����")).append(DelAnimals).append("ֻ�������").toString());
			} else {
				getServer().broadcast(ChatColor.YELLOW + "û�й�����ֳ����", "AnimalsRestrict.Command");
			}
		}

		public void Restrict(Animals RestrictThresholdCount) {
			Restrict(CheckChunkD, RestrictThresholdCount);
		}

		public void Restrict() {
			Restrict(CheckChunkD, ThresholdCount);
		}
	}

	// ж�ز��
	public void onDisable() {
		getLogger().info("AnimalRestrict����ѱ�ж�أ�");
	}

	public void AddRecipe() {
		EmeraldHelmetRecipe();
		EmeraldChestplateRecipe();
		EmeraldLeggingsRecipe();
		EmeraldBootsRecipe();

		EmeraldSpadeRecipe();
		EmeraldPickaxeRecipe();
		EmeraldAxeRecipe();

		EmeraldSwordRecipe();
		EmeraldSaddleRecipe();
		arecipe();
		arrecipe();
		parecipe();
	}

	private void EmeraldSwordRecipe() { // �̱�ʯ��
		ItemStack EmeraldSwordRecipe = new ItemStack(Material.DIAMOND_SWORD, 1);
		EmeraldSwordRecipe.addEnchantment(Enchantment.KNOCKBACK, 2);
		ItemMeta EmeraldSwordMeta = EmeraldSwordRecipe.getItemMeta();
		EmeraldSwordMeta.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("La espada de Thor").toString());
		EmeraldSwordMeta.setLore(Arrays.asList(new String[] { "����֮��" }));
		EmeraldSwordRecipe.setItemMeta(EmeraldSwordMeta);
		ShapedRecipe hrecipe = new ShapedRecipe(EmeraldSwordRecipe);
		hrecipe.shape(new String[] { "@", "@", "&" });
		hrecipe.setIngredient('@', Material.EMERALD);
		hrecipe.setIngredient('&', Material.STICK);
		Bukkit.getServer().addRecipe(hrecipe);
	}

	private void EmeraldChestplateRecipe() { // �̱�ʯ�ؼ�
		ItemStack plate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		plate.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
		plate.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
		ItemMeta pmeta = plate.getItemMeta();
		pmeta.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("El escudo de Tatuno").toString());
		pmeta.setLore(Arrays.asList(new String[] { "This are the chestplate", "of", "Tatuno!." }));
		plate.setItemMeta(pmeta);
		ShapedRecipe recipe = new ShapedRecipe(plate);
		recipe.shape(new String[] { "@ @", "@@@", "@@@" });
		recipe.setIngredient('@', Material.EMERALD);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void EmeraldLeggingsRecipe() { // �̱�ʯ����
		ItemStack boots = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		boots.addEnchantment(Enchantment.DURABILITY, 2);
		boots.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1);
		ItemMeta pmeta = boots.getItemMeta();
		pmeta.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("Los pantalones de Fichi").toString());
		pmeta.setLore(Arrays.asList(new String[] { "This are the leggins", "of", "Fichi!." }));
		boots.setItemMeta(pmeta);
		ShapedRecipe recipe = new ShapedRecipe(boots);
		recipe.shape(new String[] { "@@@", "@ @", "@ @" });
		recipe.setIngredient('@', Material.EMERALD);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void EmeraldPickaxeRecipe() { // �̱�ʯ��
		ItemStack leggins = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		leggins.addEnchantment(Enchantment.DIG_SPEED, 2);
		leggins.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta pmeta = leggins.getItemMeta();
		pmeta.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("el pico de Aunor").toString());
		pmeta.setLore(Arrays.asList(new String[] { "This is the pickaxe", "of", "Aunor!." }));
		leggins.setItemMeta(pmeta);
		ShapedRecipe recipe = new ShapedRecipe(leggins);
		recipe.shape(new String[] { "@@@", " & ", " & " });
		recipe.setIngredient('@', Material.EMERALD);
		recipe.setIngredient('&', Material.STICK);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void EmeraldHelmetRecipe() { // �̱�ʯͷ��
		ItemStack heat = new ItemStack(Material.DIAMOND_HELMET, 1);
		heat.addEnchantment(Enchantment.OXYGEN, 2);
		heat.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
		ItemMeta pmeta = heat.getItemMeta();
		pmeta.setDisplayName((new StringBuilder()).append(ChatColor.BLUE).append("el casco de Tranto").toString());
		pmeta.setLore(Arrays.asList(new String[] { "This is the helmet", "of", "Tranto!." }));
		heat.setItemMeta(pmeta);
		ShapedRecipe recipe = new ShapedRecipe(heat);
		recipe.shape(new String[] { "@@@", "@ @", "###" });
		recipe.setIngredient('@', Material.EMERALD);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void EmeraldAxeRecipe() { // �̱�ʯ��ͷ
		ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
		axe.addEnchantment(Enchantment.DIG_SPEED, 2);
		axe.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta pmeta = axe.getItemMeta();
		pmeta.setDisplayName((new StringBuilder()).append(ChatColor.BLUE).append("El Hacha de Morum").toString());
		pmeta.setLore(Arrays.asList(new String[] { "This is the axe", "of", "Morum!." }));
		axe.setItemMeta(pmeta);
		ShapedRecipe recipe = new ShapedRecipe(axe);
		recipe.shape(new String[] { "@@", "@&", " &" });
		recipe.setIngredient('@', Material.EMERALD);
		recipe.setIngredient('&', Material.STICK);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void EmeraldSpadeRecipe() { // �̱�ʯ��
		ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
		shovel.addEnchantment(Enchantment.DIG_SPEED, 2);
		shovel.addEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta meta = shovel.getItemMeta();
		meta.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("La Pala de ApoLoX").toString());
		meta.setLore(Arrays.asList(new String[] { "This is the shovel", "of", "ApoLoX!." }));
		shovel.setItemMeta(meta);
		ShapedRecipe recipe = new ShapedRecipe(shovel);
		recipe.shape(new String[] { "@", "&", "&" });
		recipe.setIngredient('@', Material.EMERALD);
		recipe.setIngredient('&', Material.STICK);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void arecipe() {
		ItemStack bfire = new ItemStack(Material.BOW, 1);
		bfire.addEnchantment(Enchantment.ARROW_FIRE, 1);
		ItemMeta bometa = bfire.getItemMeta();
		bometa.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("El arco de Farco").toString());
		bometa.setLore(Arrays.asList(new String[] { "This is the Bow", "of", "Farco!." }));
		bfire.setItemMeta(bometa);
		ShapedRecipe recipe = new ShapedRecipe(bfire);
		recipe.shape(new String[] { " @%", "@&%", " @%" });
		recipe.setIngredient('@', Material.STICK);
		recipe.setIngredient('&', Material.FLINT_AND_STEEL);
		recipe.setIngredient('%', Material.STRING);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void EmeraldBootsRecipe() {
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		boots.addEnchantment(Enchantment.DURABILITY, 1);
		boots.addEnchantment(Enchantment.PROTECTION_FALL, 2);
		ItemMeta bometa = boots.getItemMeta();
		bometa.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("Las Botas de Dios").toString());
		bometa.setLore(Arrays.asList(new String[] { "This are the boots", "of", "Good!." }));
		boots.setItemMeta(bometa);
		ShapedRecipe recipe = new ShapedRecipe(boots);
		recipe.shape(new String[] { "   ", "@ @", "@ @" });
		recipe.setIngredient('@', Material.EMERALD);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void arrecipe() {
		ItemStack boots = new ItemStack(Material.BOW, 1);
		boots.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta bometa = boots.getItemMeta();
		bometa.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("El arco de Ratuno").toString());
		bometa.setLore(Arrays.asList(new String[] { "This is the Bow", "of", "Ratuno!." }));
		boots.setItemMeta(bometa);
		ShapedRecipe recipe = new ShapedRecipe(boots);
		recipe.shape(new String[] { " @%", "@&%", " @%" });
		recipe.setIngredient('@', Material.STICK);
		recipe.setIngredient('%', Material.STRING);
		recipe.setIngredient('&', Material.REDSTONE);
		Bukkit.getServer().addRecipe(recipe);
	}

	private void EmeraldSaddleRecipe() { // �̱�ʯ����
		ItemStack EmeraldSaddle = new ItemStack(Material.DIAMOND_BARDING, 1);
		EmeraldSaddle.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
		ItemMeta EmeraldSaddleMeta = EmeraldSaddle.getItemMeta();
		EmeraldSaddleMeta.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("Esta es la silla de Mordor").toString());
		EmeraldSaddleMeta.setLore(Arrays.asList(new String[] { "ս������ " }));
		EmeraldSaddle.setItemMeta(EmeraldSaddleMeta);
		ShapedRecipe recipe = new ShapedRecipe(EmeraldSaddle);
		recipe.shape(new String[] { "  %", "%&%", "%%%" });
		recipe.setIngredient('%', Material.EMERALD_BLOCK); // �̱�ʯ
		recipe.setIngredient('&', Material.SADDLE); // ��
		Bukkit.getServer().addRecipe(recipe);
	}

	private void parecipe() {
		ItemStack lc = new ItemStack(Material.STICK, 1);
		lc.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		lc.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
		lc.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		lc.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);
		ItemMeta lcmeta = lc.getItemMeta();
		lcmeta.setDisplayName((new StringBuilder()).append(ChatColor.GRAY).append("Esta es la silla de Mordor").toString());
		lcmeta.setLore(Arrays.asList(new String[] { "This is the Horse Armor ", "of", "Mordor!." }));
		lc.setItemMeta(lcmeta);
		ShapedRecipe recipe = new ShapedRecipe(lc);
		recipe.shape(new String[] { "/$2", "%&%", "21/" });
		recipe.setIngredient('%', Material.DIAMOND_SWORD);
		recipe.setIngredient('&', Material.STICK);
		recipe.setIngredient('/', Material.GOLD_BLOCK);
		recipe.setIngredient('$', Material.NETHER_STAR);
		recipe.setIngredient('1', Material.DIAMOND);
		recipe.setIngredient('2', Material.REDSTONE_LAMP_OFF);
		Bukkit.getServer().addRecipe(recipe);
	}

}
