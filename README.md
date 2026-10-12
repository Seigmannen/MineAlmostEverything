# Mine (Almost) Everything

A Fabric mod for Minecraft 26.3 that lets you mine blocks you normally can't get:
unbreakable blocks with a netherite pickaxe, and budding amethyst and spawners with Silk Touch.

## What it does

| Block | Tool needed | What you get |
| --- | --- | --- |
| Bedrock | Netherite pickaxe | The block itself |
| Reinforced deepslate | Netherite pickaxe | The block itself |
| Budding amethyst | Iron, diamond or netherite pickaxe with **Silk Touch** | The block itself |
| Spawner | Iron, diamond or netherite pickaxe with **Silk Touch** | The spawner, which keeps its mob type when placed |

## Netherite pickaxe: unbreakable blocks

- A **netherite pickaxe** held in your main hand can mine **bedrock** and **reinforced deepslate**.
- Only the netherite pickaxe gets these rules. Reinforced deepslate can still be broken slowly
  with other tools like in vanilla, but then it drops nothing.
- A block mined with a netherite pickaxe **drops as itself**, so you can pick it up and place it again.
- This works on **all bedrock**, including the bottom layer of the Overworld (y = −64).
  Careful: breaking that layer opens a hole into the void.

### Mining time

Mining one of these blocks takes **2 minutes** with no upgrades.

| Bonus | Effect on mining time |
| --- | --- |
| Efficiency enchantment | −15 seconds per level |
| Haste effect | −20 seconds per level |
| Minimum time | never less than 5 seconds |

Formula: `time = 120 − 15 × Efficiency level − 20 × Haste level` seconds, but at least 5 seconds.

Examples:

| Pickaxe and effects | Time |
| --- | --- |
| No enchantments, no Haste | 120 s |
| Efficiency V | 45 s |
| Haste II (e.g. from a beacon) | 80 s |
| Efficiency V + Haste I | 25 s |
| Efficiency V + Haste II | 5 s |

The time is fixed by these rules only. Unlike normal blocks, mining these blocks is **not** slowed down
by Mining Fatigue, being underwater or not standing on the ground.

### Durability

- Each of these blocks costs the pickaxe **10 durability** instead of the usual 1.
- **Unbreaking works as normal**: it is rolled separately for each of the 10 points.
  On average a block costs about 5 durability with Unbreaking I, 3.3 with Unbreaking II
  and 2.5 with Unbreaking III.
- The pickaxe can break while mining, just like any other tool.

## Silk Touch: budding amethyst and spawners

- **Budding amethyst** and **spawners** need an **iron, diamond or netherite pickaxe**.
  Wooden, stone, golden and copper pickaxes mine them slowly and get nothing.
- With **Silk Touch**, the block drops as itself.
- Without Silk Touch they work like in vanilla: budding amethyst drops nothing,
  and a spawner drops nothing but gives XP.
- A spawner mined with Silk Touch **gives no XP**, so you can't get both the XP and the spawner.
- The dropped spawner item **remembers its mob type**, and keeps it when you place it again.

### Placing spawners

- A spawner mined with Silk Touch keeps its mob type when placed, **also for players who are not operators**.
  (In vanilla, only operators can place spawners with data.)
- For safety, **only the mob type** is read from the item. All other spawner data
  (spawn delay, range, mob equipment and so on) is ignored, so the spawner works like a normal one.
- Only mobs that have a **spawn egg in vanilla** are allowed. Anything else
  (for example a command block minecart) gives an empty spawner.
- **Operators** placing a spawner with full block entity data get all the data, like in vanilla.
- This is handled by the server.

## Creative mode

In Creative mode all these blocks are broken instantly as usual. They do not drop as items,
and the pickaxe takes no damage.

## Data-driven rules

Which blocks get these rules is decided by data files (tags and loot tables), not by code:

| File | What it does |
| --- | --- |
| `data/mineeverything/tags/block/netherite_mineable.json` | The blocks the netherite pickaxe can mine with the 2-minute rule. |
| `data/minecraft/tags/block/needs_iron_tool.json` | Adds budding amethyst and spawner to vanilla's "needs at least an iron tool" list. |
| `data/minecraft/loot_table/blocks/<block>.json` | What each block drops (bedrock, reinforced deepslate, budding amethyst, spawner). |

A datapack can add more blocks to the `mineeverything:netherite_mineable` tag.
Give each new block a loot table too, or it will drop nothing.

## Requirements

| Software | Version |
| --- | --- |
| Minecraft (Java Edition) | 26.3 |
| Fabric Loader | 0.19.5 or newer |
| Fabric API | for 26.3 (built with 0.162.0+26.3) |
| Java | 25 or newer |

## Installation

1. Install the [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 26.3.
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for 26.3.
3. Put the Fabric API jar and the Mine (Almost) Everything jar (`mineeverything-1.0.0.jar`) in your `mods` folder.
   - Windows: `%appdata%\.minecraft\mods`
4. Start Minecraft with the Fabric profile.

**Multiplayer:** the mod must be installed on **both the server and every player's client**.
The client and server have to agree on how fast these blocks break.

## Building from source

```
./gradlew build
```

The mod jar ends up in `build/libs/mineeverything-1.0.0.jar`
(the file ending in `-sources.jar` contains only the source code).

To test in a development game, run `./gradlew runClient`.

## License

This mod is available under the [MIT License](LICENSE).
