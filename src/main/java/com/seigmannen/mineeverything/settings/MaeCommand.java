package com.seigmannen.mineeverything.settings;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;

/**
 * /mae                         shows all mining settings (anyone can use it).
 * /mae <setting> <true|false>  turns a setting on or off for this world (operators only).
 */
public final class MaeCommand {
	private MaeCommand() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> mae = Commands.literal("mae")
				.executes(context -> showSettings(context.getSource()));

		for (MiningSetting setting : MiningSetting.values()) {
			mae.then(Commands.literal(setting.getName())
					.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
					.then(Commands.argument("value", BoolArgumentType.bool())
							.executes(context -> setSetting(context.getSource(), setting, BoolArgumentType.getBool(context, "value")))));
		}

		dispatcher.register(mae);
	}

	private static int showSettings(CommandSourceStack source) {
		MinecraftServer server = source.getServer();
		MutableComponent message = Component.literal("Mine (Almost) Everything settings:");
		for (MiningSetting setting : MiningSetting.values()) {
			boolean enabled = MiningSettings.isEnabled(server, setting);
			message.append("\n  " + setting.getName() + ": ")
					.append(Component.literal(String.valueOf(enabled)).withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED));
		}
		source.sendSuccess(() -> message, false);
		return MiningSettings.getEnabled(server).size();
	}

	private static int setSetting(CommandSourceStack source, MiningSetting setting, boolean enabled) {
		MiningSettings.set(source.getServer(), setting, enabled);
		source.sendSuccess(() -> Component.literal("Mine (Almost) Everything: " + setting.getName() + " is now " + enabled), true);
		return enabled ? 1 : 0;
	}
}
