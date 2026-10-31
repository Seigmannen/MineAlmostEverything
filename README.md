# Mine (Almost) Everything

A Fabric mod for Minecraft 26.3 that lets you mine blocks you normally can't get:
unbreakable blocks with a netherite pickaxe, budding amethyst and spawners with Silk Touch,
and cobwebs with a Silk Touch pickaxe.

## What it does

| Block | Tool needed | What you get |
| --- | --- | --- |
| Bedrock | Netherite pickaxe | The block itself |
| Reinforced deepslate | Netherite pickaxe | The block itself |
| End portal frame | Netherite pickaxe | The frame, without an Eye of Ender |
| Budding amethyst | Iron, diamond or netherite pickaxe with **Silk Touch** | The block itself |
| Spawner | Iron, diamond or netherite pickaxe with **Silk Touch** | The spawner, which keeps its mob type when placed |
| Cobweb | Any pickaxe with **Silk Touch** | The block itself |

## Netherite pickaxe: unbreakable blocks

- A **netherite pickaxe** held in your main hand can mine **bedrock**, **reinforced deepslate**
  and **end portal frames**.
- Only the netherite pickaxe gets these rules. Reinforced deepslate can still be broken slowly
  with other tools like in vanilla, but then it drops nothing.
- A block mined with a netherite pickaxe **drops as itself**, so you can pick it up and place it again.
- This works on **all bedrock**, including the bottom layer of the Overworld (y = −64)
  and the bottom (y = 0) and roof (y = 127) of the Nether.
  Operators can turn each of these layers off with `/mae overworld_bottom false`, `/mae nether_bottom false`
  and `/mae nether_roof false` (see [Settings](#settings-per-world)).
  Careful: breaking a bottom layer opens a hole into the void.

### End portal frames

- A mined end portal frame always drops **an empty frame**. If it had an Eye of Ender in it, **the eye is lost**.
- If the frame was part of an **active portal**, the portal disappears when the frame is broken.
  This also happens when the frame is removed in another way, for example by a quarry or with `/setblock`.
  To get the portal back, place the frame again and put an eye in it.

### Mining time

Mining one of these blocks takes **2 minutes** with no upgrades.

| Bonus                  | Effect on mining time     |
| ---------------------- | ------------------------- |
| Efficiency enchantment | −15 seconds per level     |
| Haste effect           | −20 seconds per level     |
| Minimum time           | never less than 5 seconds |

Formula: `time = 120 − 15 × Efficiency level − 20 × Haste level` seconds, but at least 5 seconds.

Examples:

| Pickaxe and effects           | Time  |
| ----------------------------- | ----- |
| No enchantments, no Haste     | 120 s |
| Efficiency V                  | 45 s  |
| Haste II (e.g. from a beacon) | 80 s  |
| Efficiency V + Haste I        | 25 s  |
| Efficiency V + Haste II       | 5 s   |

The time is fixed by these rules only. Unlike normal blocks, mining these blocks is **not** slowed down
by Mining Fatigue, being underwater or not standing on the ground.

### Durability

- Each of these blocks costs the pickaxe **10 durability** instead of the usual 1.
- **Unbreaking works as normal**: it is rolled separately for each of the 10 points.
  On average a block costs about 5 durability with Unbreaking I, 3.3 with Unbreaking II
  and 2.5 with Unbreaking III.
- The pickaxe can break while mining, just like any other tool.

## Silk Touch: budding amethyst and spawners

- With an **iron, diamond or netherite pickaxe** with **Silk Touch**, the block drops as itself.
- In every other case they work exactly like in vanilla (same mining speed with every pickaxe):
  budding amethyst drops nothing, and a spawner drops nothing but gives XP.
  This includes Silk Touch on a wooden, stone, golden or copper pickaxe.
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

## Silk Touch pickaxe: cobwebs

- **Any pickaxe** with **Silk Touch** makes a cobweb drop itself. This uses the `minecraft:pickaxes` tag,
  so it also works with pickaxes from other mods that are in that tag.
- Cobwebs break **as fast as in vanilla**; Silk Touch only changes what they drop.
- Everything else is like vanilla: shears and Silk Touch swords still give the cobweb, swords without
  Silk Touch give string, and a pickaxe without Silk Touch gives nothing.

## Settings (per world)

Operators can turn each part of the mod on or off for a world with the `/mae` command.
Everything is **on** by default.

| Command | Who | What it does |
| --- | --- | --- |
| `/mae` | Everyone | Shows all settings and whether they are on. |
| `/mae <setting> <true\|false>` | Operators | Turns a setting on or off, e.g. `/mae overworld_bottom false`. |

Each setting only affects its own block:

| Setting | When off |
| --- | --- |
| `bedrock` | Bedrock can't be mined anywhere, like in vanilla (whatever `overworld_bottom`, `nether_bottom` and `nether_roof` are set to). |
| `reinforced_deepslate` | Reinforced deepslate works like in vanilla: slow to break and drops nothing. |
| `budding_amethyst` | Budding amethyst drops nothing, even with Silk Touch. |
| `spawner` | Spawners drop nothing with Silk Touch, and give XP like in vanilla. |
| `end_portal_frame` | End portal frames can't be mined, like in vanilla. Removing a frame in another way (like `/setblock`) leaves the portal. |
| `cobweb` | A Silk Touch pickaxe gets nothing from cobwebs, like in vanilla. |
| `overworld_bottom` | Bedrock on the lowest layer of the **Overworld** (y = −64) can't be mined. Other bedrock in the Overworld still can. |
| `nether_bottom` | Bedrock on the lowest layer of the **Nether** (y = 0) can't be mined. Other bedrock in the Nether still can. |
| `nether_roof` | Bedrock on the top layer of the **Nether** roof (y = 127) can't be mined. Other bedrock in the Nether still can. |

All other bedrock (the rest of the Nether, the End and other dimensions) only follows `bedrock`.

The settings are saved in the world folder (`data/mineeverything/settings.dat`), so each world has its own,
and they are kept when the game or server restarts.

Good to know:

- Spawner items you already have keep their mob type when placed, even with `spawner` off.

### For other mods

The drops are checked by the loot condition `{"type": "mineeverything:mining_allowed"}` in this mod's loot tables,
so blocks broken by machines (like a quarry) also follow the settings: a block that is turned off drops nothing.
A mod that wants to know *before* breaking a block, or wants to read the settings, can use the public API in
`com.seigmannen.mineeverything.api.MineEverythingApi`:

| Method | Returns |
| --- | --- |
| `MineEverythingApi.isEnabled(Level level, String setting)` | `true` if that `/mae` setting is on in this world. `setting` is one of `"bedrock"`, `"reinforced_deepslate"`, `"budding_amethyst"`, `"spawner"`, `"overworld_bottom"`, `"nether_bottom"`, `"nether_roof"`, `"end_portal_frame"`, `"cobweb"`. An unknown name gives `false`. |
| `MineEverythingApi.isAllowed(Level level, BlockState state, BlockPos pos)` | `true` if the settings allow this mod's rules for that block at that position (for bedrock it also checks `overworld_bottom` at y = −64 in the Overworld, and `nether_bottom` at y = 0 and `nether_roof` at y = 127 in the Nether). Blocks without a setting give `true`. |
| `MineEverythingApi.canMineUnbreakable(Level level, BlockState state, BlockPos pos, ItemStack tool)` | `true` if `tool` can mine that block by this mod's rules for blocks that are unbreakable in vanilla: the block is in the `mineeverything:netherite_mineable` tag, `tool` is a netherite pickaxe, and the settings allow it (including the bedrock layer settings). These are the same rules a player is held to. |
| `MineEverythingApi.canHarvestWith(Level level, BlockState state, BlockPos pos, ItemStack tool)` | `true` if this mod's rules make `tool` the correct tool for drops from that block, with no player needed (for example any pickaxe with Silk Touch on a cobweb). Covers all of this mod's "correct tool" rules, so new rules are included automatically, and follows their settings (a rule that is turned off gives `false`). Vanilla's own rules are not included, so a quarry should check both: `tool.isCorrectForDrops(state) \|\| MineEverythingApi.canHarvestWith(level, state, pos, tool)`. These are the same rules a player is held to. |

All of them work on the server and on the client (the client has a copy of the settings, sent by the server).
Only call them when this mod is loaded, so your mod still works without it:

```java
if (FabricLoader.getInstance().isModLoaded("mineeverything")) {
	boolean canMine = MineEverythingApi.canMineUnbreakable(level, state, pos, tool);
}
```

`canMineUnbreakable` was added in version 1.1.1. In version 1.2.0 the setting `bottom_layer` was replaced by
`overworld_bottom`, `nether_bottom` and `nether_roof`, so `isEnabled(level, "bottom_layer")` now gives `false`
(unknown name). The method signatures are unchanged.
Version 1.3.0 added the settings `end_portal_frame` and `cobweb`, and put end portal frames in the
`mineeverything:netherite_mineable` tag, so `canMineUnbreakable` covers them too.
Version 1.4.0 added `canHarvestWith`.

To compile against the API without making this mod required, add its jar as `compileOnly` in your `build.gradle`,
and list it under `suggests` (not `depends`) in your `fabric.mod.json`. Keep the API calls in a class that is only
loaded after the `isModLoaded` check, or Java may fail to find `MineEverythingApi` when the mod is missing.

## Creative mode

In Creative mode all these blocks are broken instantly as usual. They do not drop as items,
and the pickaxe takes no damage.

## Data-driven rules

Which blocks get these rules is decided by data files (tags and loot tables), not by code:

| File | What it does |
| --- | --- |
| `data/mineeverything/tags/block/netherite_mineable.json` | The blocks the netherite pickaxe can mine with the 2-minute rule (bedrock, reinforced deepslate, end portal frame). |
| `data/mineeverything/tags/item/silk_touch_pickaxes.json` | The pickaxes that, with Silk Touch, make budding amethyst and spawners drop themselves (iron, diamond, netherite). |
| `data/minecraft/loot_table/blocks/<block>.json` | What each block drops (bedrock, reinforced deepslate, end portal frame, budding amethyst, spawner). Each one uses the `mineeverything:mining_allowed` condition, so it follows the settings. |

Cobwebs use vanilla's own loot table, which already drops the cobweb with Silk Touch. Vanilla just doesn't
give a pickaxe any drops from cobwebs, and that is the only thing this mod changes for them.

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
3. Put the Fabric API jar and the Mine (Almost) Everything jar (`mineeverything-1.4.0.jar`) in your `mods` folder.
   - Windows: `%appdata%\.minecraft\mods`
4. Start Minecraft with the Fabric profile.

**Multiplayer:** the mod must be installed on **both the server and every player's client**.
The client and server have to agree on how fast these blocks break.

## Building from source

```
./gradlew build
```

The mod jar ends up in `build/libs/mineeverything-1.4.0.jar`
(the file ending in `-sources.jar` contains only the source code).

To test in a development game, run `./gradlew runClient`.

## License

This mod is available under the [MIT License](LICENSE).
