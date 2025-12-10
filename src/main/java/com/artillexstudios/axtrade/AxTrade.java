package com.artillexstudios.axtrade;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.config.Config;
import com.artillexstudios.axapi.libs.boostedyaml.dvs.versioning.BasicVersioning;
import com.artillexstudios.axapi.libs.boostedyaml.settings.dumper.DumperSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.general.GeneralSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.loader.LoaderSettings;
import com.artillexstudios.axapi.libs.boostedyaml.settings.updater.UpdaterSettings;
import com.artillexstudios.axapi.utils.MessageUtils;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axtrade.commands.CommandManager;
import com.artillexstudios.axtrade.hooks.HookManager;
import com.artillexstudios.axtrade.lang.LanguageManager;
import com.artillexstudios.axtrade.listeners.EntityInteractListener;
import com.artillexstudios.axtrade.listeners.TradeListeners;
import com.artillexstudios.axtrade.safety.SafetyManager;
import com.artillexstudios.axtrade.trade.TradeTicker;
import com.artillexstudios.axtrade.utils.NumberUtils;
import com.chickennw.utils.ChickenUtils;
import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;

import java.io.File;

public final class AxTrade extends AxPlugin {

    public static Config CONFIG;
    public static Config LANG;
    public static Config GUIS;
    public static Config HOOKS;
    public static Config TOGGLED;
    public static MessageUtils MESSAGEUTILS;
    public static BukkitAudiences BUKKITAUDIENCES;

    @Getter
    private static AxPlugin instance;

    @Getter
    private static FoliaLib foliaLib;

    public void enable() {
        instance = this;
        foliaLib = new FoliaLib(this);
        ChickenUtils.setPlugin(this);

        CONFIG = new Config(new File(getDataFolder(), "config.yml"), getResource("config.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("version")).build());
        GUIS = new Config(new File(getDataFolder(), "guis.yml"), getResource("guis.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("version")).build());
        LANG = new Config(new File(getDataFolder(), "lang.yml"), getResource("lang.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("version")).build());
        HOOKS = new Config(new File(getDataFolder(), "currencies.yml"), getResource("currencies.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("version")).build());
        TOGGLED = new Config(new File(getDataFolder(), "toggled.yml"), getResource("toggled.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.DEFAULT, DumperSettings.DEFAULT, UpdaterSettings.DEFAULT);

        LanguageManager.reload();

        MESSAGEUTILS = new MessageUtils(LANG.getBackingDocument(), "prefix", CONFIG.getBackingDocument());

        BUKKITAUDIENCES = BukkitAudiences.create(this);

        getServer().getPluginManager().registerEvents(new EntityInteractListener(), this);
        getServer().getPluginManager().registerEvents(new TradeListeners(), this);

        HookManager.setupHooks();
        NumberUtils.reload();

        TradeTicker.start();
        SafetyManager.start();
        CommandManager.load();

        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#00FFDD[AxTrade] Loaded plugin!"));
    }

    public void disable() {
        SafetyManager.stop();
        com.chickennw.utils.managers.HookManager.getInstance().unloadAllHooks();
    }

    public void updateFlags() {
        FeatureFlags.USE_LEGACY_HEX_FORMATTER.set(true);
        FeatureFlags.PLACEHOLDER_API_HOOK.set(true);
        FeatureFlags.PLACEHOLDER_API_IDENTIFIER.set("axtrade");
    }
}