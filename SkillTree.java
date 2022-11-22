/*
name: Thang Nguyen
nsid: dun329
student number:11275930
course number:CMPT280
*/

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.BasicMAryTree280;
import lib280.list.LinkedList280;

public class SkillTree extends BasicMAryTree280<Skill> {

	/**	
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.  
	 * @timing O(1) 
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes 
	 */
	public SkillTree(Skill x, int m)
	{
		super(x,m);
	}

	/**
	 * A convenience method that avoids typecasts.
	 * Obtains a subtree of the root.
	 * 
	 * @param i Index of the desired subtree of the root.
	 * @return the i-th subtree of the root.
	 */
	public SkillTree rootSubTree(int i) {
		return (SkillTree)super.rootSubtree(i);
	}


	/**
	 * Search if the skill is contained in the skill tree
	 *
	 * @param root         skill name search (get recursive through the tree to find wanted skill name)
	 * @param skillName    name of the skill
	 * @param dependencies list of skill contain in node need to acquire to get that skill name
	 * @return True if skill can be found, False otherwise
	 */
	protected boolean Found(SkillTree root, String skillName, LinkedList280<Skill> dependencies)
			throws ContainerEmpty280Exception {

		try {
			//if we found the wanted skill , then insert is to list
			String rootSkill = root.rootItem().getSkillName();
			if (rootSkill.equals(skillName)) {
				dependencies.insert(root.rootItem());
				return true;
			}
			// for non-empty subtree, use recursive traversal of m-ary trees to find the desirable skill
			int i = 1;
			do {
				if (Found(root.rootSubTree(i), skillName, dependencies)) {
					dependencies.insert(root.rootItem());
					return true;
				}
				i = i + 1;
			}
			while (i <= rootLastNonEmptyChild());
			// return false if skill can't be found
			return false;
		} catch (ContainerEmpty280Exception e) {
			return false;
		}

	}


	/**
	 * Search if the skill is contained in the skill tree
	 *
	 * @param skillName name of the skill
	 * @return list of skill
	 */
	public LinkedList280<Skill> skillDependencies(String skillName) throws ItemNotFound280Exception {
		LinkedList280<Skill> dependencies = new LinkedList280<>();

		// if skill can't be found in the tree then throw exception
		SkillTree rootData = this;
		if (!Found(rootData, skillName, dependencies)) {
			throw new ItemNotFound280Exception("That skill cannot be found in the skill tree.");
		} else {
			return dependencies;
		}

	}

	/**
	 * Calculate the amount of point user need to spend to get a skill
	 *
	 * @param skillName name of the skill
	 * @return total points
	 */
	public int skillTotalCost(String skillName) throws ItemNotFound280Exception {
		LinkedList280<Skill> dependencies; // create a list contain dependence skill
		dependencies = skillDependencies(skillName); // get the list of spell that are dependency of wanted skill
		dependencies.goFirst(); // set cursor position to the start of the list

		if (!dependencies.itemExists()) {
			throw new ItemNotFound280Exception("Can not find total points from an empty list");
		} else {

			// loop through the list and add all spell cost that are dependency of wanted skill
			int totalSpellCost = 0;
			do {
				int skillCost = dependencies.item().getSkillCost();
				totalSpellCost = totalSpellCost + skillCost;
				dependencies.goForth();
			} while (dependencies.itemExists());

			return totalSpellCost;
		}
	}


	public static void main(String[] args) {
		System.out.println("Skill tree for upgrading Skyrim's Dragonborn abilities: ");

		// All skills definition
		Skill CombatArt = new Skill("Combat Art", "Unique ability that can damage dragon from the elder scroll", 1);
		Skill Destruction = new Skill("Destruction", "Normal attack that can punch through dragon skin", 2);
		Skill RawPower = new Skill("Raw Power", "Increase Physical strength and heat resistance from dragon breath", 5);
		Skill RuneMaster = new Skill("Rune Master", "Magical power that inherit from blood of god", 1);
		Skill ElementalShield = new Skill("Elemental Shield", "Create a shield that similar to dragon breath property ", 4);
		Skill Illusion = new Skill("Illusion", "Put opponent on third dimension and increase owner soul power", 3);
		Skill Fear = new Skill("Fear", "A big roar that can fear and paralyze opponent", 2);
		Skill WarSpirit = new Skill("War Spirit", "Reduce owner fear to zero and minimize pain", 4);
		Skill Enchanting = new Skill("Enchanting", "Self-made counter potion and magic rune ", 2);
		Skill ManaStone = new Skill("Mana Stone", " 60s rage mode, unlimited mana to cast spell, 48 hours cool down", 6);
		Skill Block = new Skill("Block", "Immune to normal weapon that used by opponent that have level smaller than 10", 1);
		Skill Counter = new Skill("Counter", " Reflex damage from an attack ", 2);

		// Define the tree skill
		SkillTree T = new SkillTree(CombatArt, 4);
		T.setRootSubtree(new SkillTree(Destruction, 4), 1);
		T.setRootSubtree(new SkillTree(Illusion, 4), 2);
		T.setRootSubtree(new SkillTree(Enchanting, 4), 3);
		T.setRootSubtree(new SkillTree(Block, 4), 4);

		// Define skill for destruction
		SkillTree data = T.rootSubTree(1);
		data.setRootSubtree(new SkillTree(RawPower, 4), 1);
		data.setRootSubtree(new SkillTree(RuneMaster, 4), 2);

		SkillTree data2 = data.rootSubTree(2);
		data2.setRootSubtree(new SkillTree(ElementalShield, 4), 1);

		// Define skill for illusion
		data = T.rootSubTree(2);
		data.setRootSubtree(new SkillTree(Fear, 4), 1);
		data.setRootSubtree(new SkillTree(WarSpirit, 4), 2);

		// Define skill for enchanting
		data = T.rootSubTree(3);
		data.setRootSubtree(new SkillTree(ManaStone, 4), 1);

		// Define skill for Block
		data = T.rootSubTree(4);
		data.setRootSubtree(new SkillTree(Counter, 4), 2);

		System.out.println(T.toStringByLevel());


		// check all skill dependency for elemental shield
		System.out.println("Dependencies for Elemental Shield: ");
		System.out.println(T.skillDependencies("Elemental Shield"));

		// check all skill dependency for Enchanting
		System.out.println("Dependencies for Enchanting: ");
		System.out.println(T.skillDependencies("Enchanting"));

		// check all skill dependency for War Spirit
		System.out.println("Dependencies for War Spirit: ");
		System.out.println(T.skillDependencies("War Spirit"));

		// check all skill dependency for Block
		System.out.println("Dependencies for Block: ");
		System.out.println(T.skillDependencies("Block"));

		// Check skill that not exist in the tree
		try {
			System.out.println("Dependencies for Explosion mana bom: ");
			System.out.println(T.skillDependencies("Explosion mana bom"));
		} catch (RuntimeException e) {
			System.out.println("Explosion mana bom not found.");
		}

		// Point need to invest in order to have Fear
		int FearRequirePoint = T.skillTotalCost("Fear");
		System.out.println("To get Fear you must invest " + FearRequirePoint + " points.");

		// Point need to invest in order to have War Spirit
		int SpiritPoint = T.skillTotalCost("War Spirit");
		System.out.println("To get War Spirit you must invest " + SpiritPoint + " points.");

		// Point need to invest in order to have Rune Master
		int RuneMasterRequirePoint = T.skillTotalCost("Rune Master");
		System.out.println("To get Rune Master you must invest " + RuneMasterRequirePoint + " points.");

		// Point need to invest in order to have Rune Master
		int CombatArtRequirePoint = T.skillTotalCost("Combat Art");
		System.out.println("To get Combat Art you must invest " + CombatArtRequirePoint + " points.");

		// check case for skill not in the tree
		int ExplosionCost = 5;
		try {
			ExplosionCost = T.skillTotalCost("Explosion mana bom");
			System.out.println("To get Explosion mana bom you must invest " + ExplosionCost + " points.");
		} catch (RuntimeException e) {
			System.out.println("Explosion mana bom not found.");
		}

	}
}
