package me.Seisan.plugin.Features.Chat;

import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.commands.anothers.PrefixCommand;
import me.Seisan.plugin.Features.utils.Channel;
import me.Seisan.plugin.Features.utils.ItemUtil;
import me.Seisan.plugin.Main;
import me.Seisan.plugin.Features.Feature;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ChatFormat extends Feature {
    private static List<String> PREFIX;

    // Map of player to list their message ending with > to send everything at once
    private static Map<Player, String> halfWrittenMessage = new HashMap<>();

    private static void addPrefix(String prefix) {
        PREFIX.add(prefix);
    }

    public static List<String> getPrefix() {
        return PREFIX;
    }

    public void addExecutor(String prefix, EventExecutor eventExecutor) {
        Main.plugin().getServer().getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, EventPriority.LOW, eventExecutor, Main.plugin(), true);
        Main.log(Level.INFO, "Added chat format rule " + prefix);

    }

    private void addExecutor(String prefix, EventExecutor eventExecutor, String rule) {
        Main.plugin().getServer().getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, EventPriority.LOW, eventExecutor, Main.plugin(), true);
        Main.log(Level.INFO, "Added chat format rule for prefix " + prefix + " : \"" + rule + "\".");

    }

    public void removeExecutor(String prefix) {
        PREFIX = new ArrayList<String>();
        resetPrefix(prefix);
        removeAll();
        registerAll();
    }

    private void resetPrefix(String prefix) {
        Main.CONFIG.addDefault(path + "rule." + prefix, null);
        Main.CONFIG.set(path + "rule." + prefix, null);
        Main.plugin().saveConfig();
    }

    public void resetAll() {
        PREFIX = new ArrayList<>();
        Main.CONFIG.addDefault(path + "rule", null);
        Main.CONFIG.set(path + "rule", null);
        Main.plugin().saveConfig();
    }

    private void removeAll() {
        HandlerList.unregisterAll(this);
    }

    private void registerAll() {
        ConfigurationSection prefixs = Main.CONFIG.getConfigurationSection(path + "rule");
        for (String preKey : prefixs.getKeys(false)) {
            registerRule("default".equals(preKey) ? "" : preKey, prefixs.getString(preKey));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().contains("%")) {
            //      event.setMessage(event.getMessage().replace("%", ""));
        }
    }

    private void addRule(String prefix, String rule) {
        addConfig("rule." + prefix, rule);
        Main.plugin().saveConfig();
    }

    private boolean registerRule(String prefix, String rule) {
        final ChatFormater chatFormater = new ChatFormaterFactory().createChatFormater(rule, prefix);
        if (chatFormater == null) {
            Main.log(Level.INFO, "Syntax error on format rule " + prefix + " : \"" + rule + "\".");
            return false;
        }
        EventExecutor executor = new EventExecutor() {
            ChatFormater innerChatFormater = chatFormater;

            @Override
            public void execute(Listener listener, Event event) throws EventException {
                AsyncPlayerChatEvent chatEvent = (AsyncPlayerChatEvent) event;

                // If message ends with ">", we store it in a map to send it all at once
                String message = chatEvent.getMessage();
                if (message.endsWith(">")) {
                    if (halfWrittenMessage.containsKey(chatEvent.getPlayer())) {
                        // Remove the ">" at the end of the message
                        halfWrittenMessage.put(chatEvent.getPlayer(), halfWrittenMessage.get(chatEvent.getPlayer()) + " " + chatEvent.getMessage().substring(0, chatEvent.getMessage().length() - 1));
                    } else {
                        halfWrittenMessage.put(chatEvent.getPlayer(), chatEvent.getMessage().substring(0, chatEvent.getMessage().length() - 1));
                    }
                    chatEvent.setCancelled(true);
                } else {
                    if (halfWrittenMessage.containsKey(chatEvent.getPlayer())) {
                        message = halfWrittenMessage.get(chatEvent.getPlayer()) + " " + chatEvent.getMessage();
                        halfWrittenMessage.remove(chatEvent.getPlayer());
                        chatEvent.setMessage(message);
                    }

                    if (innerChatFormater.isGoodPrefix(PrefixCommand.getPlayerDefaultPrefix(chatEvent.getPlayer()) + chatEvent.getMessage())) {
                        chatEvent.setCancelled(true);
                        chatEvent.setMessage(PrefixCommand.getPlayerDefaultPrefix(chatEvent.getPlayer()) + chatEvent.getMessage());
                        FormatedMessageSender.send(innerChatFormater.formatMessage(chatEvent));
                    }
                }
            }
        };
        addExecutor(prefix, executor, rule);
        return true;
    }

    /**
     * Initialize the default set of chat rules.
     */
    private void initialize() {
        /* Speaking */
        addRule("#", "{range:3}<%a [chuchote]> {color:DARK_AQUA}%m");
        addRule("-", "{range:7}<%a [parle bas]> {color:DARK_GREEN}%m");
        addRule("default", "{range:20}<%a [dit]> {color:GREEN}%m");
        addRule("+", "{range:50}<%a [parle fort]> {color:YELLOW}%m");
        addRule("!", "{range:100}<%a [crie]> {color:RED}%m");

        /* Acting */
        addRule("*#", "{range:3}{color:DARK_AQUA,italic:true}*%a %m*");
        addRule("#*", "{range:3}{color:DARK_AQUA,italic:true}*%a %m*");
        addRule("*#,", "{range:3}{color:DARK_AQUA,italic:true}*%a, %m*");
        addRule("#*,", "{range:3}{color:DARK_AQUA,italic:true}*%a, %m*");
        addRule("*-", "{range:7}{color:DARK_GREEN, italic:true}*%a %m*");
        addRule("-*", "{range:7}{color:DARK_GREEN, italic:true}*%a %m*");
        addRule("*-,", "{range:7}{color:DARK_GREEN, italic:true}*%a, %m*");
        addRule("-*,", "{range:7}{color:DARK_GREEN, italic:true}*%a, %m*");
        addRule("*", "{range:20}{color:GREEN, italic:true}*%a %m*");
        addRule("*,", "{range:20}{color:GREEN, italic:true}*%a, %m*");
        addRule("*+", "{range:50}{color:YELLOW, italic:true}*%a %m*");
        addRule("+*", "{range:50}{color:YELLOW, italic:true}*%a %m*");
        addRule("*+,", "{range:50}{color:YELLOW, italic:true}*%a, %m*");
        addRule("+*,", "{range:50}{color:YELLOW, italic:true}*%a, %m*");
        addRule("*!", "{range:100}{color:RED, italic:true}*%a %m*");
        addRule("!*", "{range:100}{color:RED, italic:true}*%a %m*");
        addRule("*!,", "{range:100}{color:RED, italic:true}*%a, %m*");
        addRule("!*,", "{range:100}{color:RED, italic:true}*%a, %m*");
        addRule("**", "{range:30}{color:BLUE}**[%a] %m**");

        /* Animal */
        /* Speaking */
        addRule(";#", "{range:3, animal:true}<%a [chuchote]> {color:DARK_AQUA}%m");
        addRule(";-", "{range:7, animal:true}<%a [parle bas]> {color:DARK_GREEN}%m");
        addRule(";", "{range:20, animal:true}<%a [dit]> {color:GREEN}%m");
        addRule(";+", "{range:50, animal:true}<%a [parle fort]> {color:YELLOW}%m");
        addRule(";!", "{range:100, animal:true}<%a [crie]> {color:RED}%m");

        /* Acting */
        addRule(";*#", "{range:3, animal:true}{color:DARK_AQUA,italic:true}*%a %m*");
        addRule(";#*", "{range:3, animal:true}{color:DARK_AQUA,italic:true}*%a %m*");
        addRule(";*-", "{range:7, animal:true}{color:DARK_GREEN, italic:true}*%a %m*");
        addRule(";-*", "{range:7, animal:true}{color:DARK_GREEN, italic:true}*%a %m*");
        addRule(";*", "{range:20, animal:true}{color:GREEN, italic:true}*%a %m*");
        addRule(";*+", "{range:50, animal:true}{color:YELLOW, italic:true}*%a %m*");
        addRule(";+*", "{range:50, animal:true}{color:YELLOW, italic:true}*%a %m*");
        addRule(";*!", "{range:100, animal:true}{color:RED, italic:true}*%a %m*");
        addRule(";!*", "{range:100, animal:true}{color:RED, italic:true}*%a %m*");

        /* Encadrement */

        addRule("?#", "{range:3}{restricted:enca, color:AQUA}** %m");
        addRule("#?", "{range:3}{restricted:enca, color:AQUA}** %m");
        addRule("-?", "{range:7}{restricted:enca, color:AQUA}** %m");
        addRule("?-", "{range:7}{restricted:enca, color:AQUA}** %m");
        addRule("?", "{range:20}{restricted:enca, color:AQUA}** %m");
        addRule("?+", "{range:50}{restricted:enca, color:AQUA}** %m");
        addRule("+?", "{range:50}{restricted:enca, color:AQUA}** %m");
        addRule("?!", "{range:100}{restricted:enca, color:AQUA}** %m");
        addRule("!?", "{range:100}{restricted:enca, color:AQUA}** %m");
        addRule("?:", "{range:-1, restricted:enca, color:AQUA, foreveryworld:true}%m");
        addRule(":?", "{range:-1, restricted:enca, color:AQUA, foreveryworld:true}%m");

        addRule(":", "{range:-1, color:AQUA, foreveryworld:true}%m");

        /* Canal interstaff ou requête */
        addRule("$", "{range:-1, onlyfor:enca, foreveryworld:true}{color:#8A4000,commandonclick:@%a }<%a> %m");
        addRule("=", "{range:-1, onlyfor:mj, foreveryworld:true}{color:GOLD,commandonclick:@%a }<%a> %m");
        /* Demande d'aide au staff & réponse */
        addRule("@", "{range:-1, restricted:enca, foreveryworld:true, onlyfor:mj, addtarget:true}{color:LIGHT_PURPLE,commandonclick:$}<%a->@%t> %m");
        addRule(">", "{range:-1, onlyfor:none, addtarget:true, foreveryworld:true}{commandonclick:>%a }%a > %t :{color:GRAY} %m");
        /* HRP */
        addRule("(", "{range:20}<%a> {color:GRAY}(%m");
        Main.plugin().saveConfig();
    }

    @Override
    protected void doRegister() {
        PREFIX = new ArrayList<>();

        if (Main.CONFIG.getConfigurationSection(path + "rule") == null) {
            initialize(); // everything has been initialized so we dont need it anymore.
        }
        registerAll();
    }

    private class ChatFormaterFactory {
        private ChatFormater createChatFormater(String rule, String prefix) {
            Meta meta = new Meta();
            meta.setPrefix(prefix);
            ChatFormater chatFormater = new ChatFormater();
            RuleTokenizer ruleTokenizer = new RuleTokenizer(rule);
            Context context = new Context();

            ReadState readState = ruleTokenizer.getState();
            while (readState != ReadState.ENDED) {
                ChatElement chatElement = new ChatElement();
                switch (readState) {
                    case ERROR:
                        return null;
                    case TEXT:
                        chatElement.setType(ChatElementType.TEXT);
                        chatElement.setText(ruleTokenizer.readNext());
                        chatElement.setColor(context.getChatColor());
                        chatElement.setBold(context.isBold());
                        chatElement.setItalic(context.isItalic());
                        chatElement.setObfuscated(context.isObfuscated());
                        chatElement.setCommandOnClick(context.getCommandOnClick());
                        break;
                    case REMPLACEMENT:
                        String token = ruleTokenizer.readNext();
                        chatElement.setCommandOnClick(context.getCommandOnClick());
                        if ("a".equals(token)) {
                            chatElement.setType(ChatElementType.NAME);
                            chatElement.setBold(context.isBold());
                            chatElement.setItalic(context.isItalic());
                            chatElement.setObfuscated(context.isObfuscated());
                        } else if ("m".equals(token)) {
                            chatElement.setType(ChatElementType.MESSAGE);
                            chatElement.setColor(context.getChatColor());
                            chatElement.setBold(context.isBold());
                            chatElement.setItalic(context.isItalic());
                            chatElement.setObfuscated(context.isObfuscated());
                        } else if ("t".equals(token)) {
                            chatElement.setType(ChatElementType.TARGET);
                            chatElement.setColor(context.getChatColor());
                            chatElement.setBold(context.isBold());
                            chatElement.setItalic(context.isItalic());
                            chatElement.setObfuscated(context.isObfuscated());
                        }
                        break;
                    case ATTRIBUTE:
                        context.setCurrentAttribute(ruleTokenizer.readNext().replace(" ", ""));
                        break;
                    case PARAMETER:
                        String parameter = ruleTokenizer.readNext();
                        if ("color".equals(context.getCurrentAttribute())) {
                            try {
                                context.setChatColor(ChatColor.of(parameter));
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else if ("bold".equals(context.getCurrentAttribute())) {
                            if ("false".equals(parameter)) {
                                context.setBold(false);
                            } else {
                                context.setBold(true);
                            }
                        } else if ("italic".equals(context.getCurrentAttribute())) {
                            if ("false".equals(parameter)) {
                                context.setItalic(false);
                            } else {
                                context.setItalic(true);
                            }
                        } else if ("obfuscated".equals(context.getCurrentAttribute())) {
                            if ("false".equals(parameter)) {
                                context.setObfuscated(false);
                            } else {
                                context.setObfuscated(true);
                            }
                        } else if ("commandonclick".equals(context.getCurrentAttribute())) {
                            context.setCommandOnClick(parameter);
                        } else if ("range".equals(context.getCurrentAttribute())) {
                            try {
                                int range = Integer.valueOf(parameter);
                                meta.setRange(range);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else if ("restricted".equals(context.getCurrentAttribute())) {
                            meta.setRestriction(parameter);
                        } else if ("denialmessage".equals(context.getCurrentAttribute())) {
                            meta.setDenialMessage(parameter);
                        } else if ("onlyfor".equals(context.getCurrentAttribute())) {
                            meta.setOnlyFor(parameter);
                        } else if ("addtarget".equals(context.getCurrentAttribute())) {
                            if ("false".equals(parameter)) {
                                meta.setTargetAdded(false);
                            } else {
                                meta.setTargetAdded(true);
                            }
                        } else if ("animal".equals(context.getCurrentAttribute())) {
                            if ("false".equals(parameter)) {
                                meta.AddAnimal(false);
                            } else {
                                meta.AddAnimal(true);
                            }
                        } else if ("foreveryworld".equals(context.getCurrentAttribute())) {
                            if ("false".equals(parameter)) {
                                meta.setForEveryWorld(false);
                            } else {
                                meta.setForEveryWorld(true);
                            }
                        }
                }
                chatFormater._addChatElement(chatElement);
                readState = ruleTokenizer.getState();
            }
            chatFormater._setMeta(meta);
            addPrefix(prefix);
            return chatFormater;
        }
    }

    public class Context {
        ChatColor chatColor = ChatColor.WHITE;
        boolean bold = false;
        boolean obfuscated = false;
        boolean italic = false;
        String currentAttribute = "";
        String commandOnClick;

        private String getCurrentAttribute() {
            return currentAttribute;
        }

        private void setCurrentAttribute(String currentAttribute) {
            this.currentAttribute = currentAttribute;
        }

        private ChatColor getChatColor() {
            return chatColor;
        }

        private void setChatColor(ChatColor chatColor) {
            this.chatColor = chatColor;
        }

        private boolean isBold() {
            return bold;
        }

        private void setBold(boolean bold) {
            this.bold = bold;
        }

        private boolean isItalic() {
            return italic;
        }

        private void setItalic(boolean italic) {
            this.italic = italic;
        }

        private boolean isObfuscated() {
            return obfuscated;
        }

        private void setObfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
        }

        private String getCommandOnClick() {
            return commandOnClick;
        }

        private void setCommandOnClick(String commandOnClick) {
            this.commandOnClick = commandOnClick;
        }
    }

    public enum ReadState {
        TEXT, ATTRIBUTE, PARAMETER, REMPLACEMENT, ENDED, ERROR
    }

    public class RuleTokenizer {
        private ReadState readState;
        int i = 0;
        String rule;

        private RuleTokenizer(String rule) {
            this.rule = rule;
            if (this.rule.length() == 0) {
                this.readState = ReadState.ERROR;
            } else if (this.rule.startsWith("%")) {
                this.readState = ReadState.REMPLACEMENT;
                i++;
            } else if (this.rule.startsWith("{")) {
                this.readState = ReadState.ATTRIBUTE;
                i++;
            } else {
                this.readState = ReadState.TEXT;
            }

        }

        private ReadState getState() {
            return readState;
        }

        private String readNext() {
            StringBuilder stringBuilder = new StringBuilder();
            String ret = "";
            ReadState currentState = readState;
            switch (currentState) {
                case TEXT:
                    while (rule.length() > i && rule.charAt(i) != '%' && rule.charAt(i) != '{') {
                        if (rule.charAt(i) == '\\') {
                            i++;
                            if (rule.length() == i) {
                                break;
                            }
                        }
                        stringBuilder.append(rule.charAt(i));
                        i++;
                    }
                    if (rule.length() <= i) {
                        readState = ReadState.ENDED;
                    } else if (rule.charAt(i) == '%') {
                        readState = ReadState.REMPLACEMENT;
                        i++;
                    } else if (rule.charAt(i) == '{') {
                        readState = ReadState.ATTRIBUTE;
                        i++;
                    }
                    ret = stringBuilder.toString();
                    break;
                case ENDED:
                    break;
                case REMPLACEMENT:
                    ret = Character.toString(rule.charAt(i));
                    i++;
                    if (rule.length() <= i) {
                        readState = ReadState.ENDED;
                    } else if (rule.charAt(i) == '%') {
                        readState = ReadState.REMPLACEMENT;
                        i++;
                    } else if (rule.charAt(i) == '{') {
                        readState = ReadState.ATTRIBUTE;
                        i++;
                    } else {
                        readState = ReadState.TEXT;
                    }
                    break;
                case ATTRIBUTE:
                    while (rule.length() > i && rule.charAt(i) != ':') {
                        stringBuilder.append(rule.charAt(i));
                        i++;
                    }
                    if (rule.length() <= i) {
                        readState = ReadState.ERROR;
                    } else if (rule.charAt(i) == ':') {
                        readState = ReadState.PARAMETER;
                        i++;
                    }
                    ret = stringBuilder.toString();
                    break;
                case ERROR:
                    break;
                case PARAMETER:
                    while (rule.length() > i && rule.charAt(i) != ',' && rule.charAt(i) != '}') {
                        stringBuilder.append(rule.charAt(i));
                        i++;
                    }
                    if (rule.length() <= i) {
                        readState = ReadState.ERROR;
                    } else if (rule.charAt(i) == ',') {
                        readState = ReadState.ATTRIBUTE;
                        i++;
                    } else if (rule.charAt(i) == '}') {
                        i++;
                        switch (rule.charAt(i)) {
                            case '{':
                                readState = ReadState.ATTRIBUTE;
                                i++;
                                break;
                            case '%':
                                readState = ReadState.REMPLACEMENT;
                                i++;
                                break;
                            default:
                                readState = ReadState.TEXT;
                                break;
                        }
                    }
                    ret = stringBuilder.toString();
                    break;
            }
            return ret;
        }
    }

    public class Meta // todo : string to Type such as PenaltyType(LEVEL, FOOD) etc etc.
    {
        int range = 25;
        String prefix;
        String restriction;
        String denialMessage = ChatColor.RED + "Vous n'avez pas le droit d'utiliser ce préfixe!";
        String onlyfor;
        boolean targetAdded = false;
        boolean forEveryWorld = false;
        boolean hasAnimal = false;

        private void setRestriction(String restriction) {
            this.restriction = restriction;
        }

        private String getRestriction() {
            return restriction;
        }

        private void setDenialMessage(String denialMessage) {
            this.denialMessage = denialMessage;
        }

        private String getDenialMessage() {
            return denialMessage;
        }

        private void setRange(int range) {
            this.range = range;
        }

        private int getRange() {
            return range;
        }

        private void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }

        private String getOnlyFor() {
            return onlyfor;
        }

        private void setOnlyFor(String onlyFor) {
            this.onlyfor = onlyFor;
        }

        private boolean isTargetAdded() {
            return targetAdded;
        }

        private void setTargetAdded(boolean addTarget) {
            this.targetAdded = addTarget;
        }

        private void setForEveryWorld(boolean forEveryWorld) {
            this.forEveryWorld = forEveryWorld;
        }

        private boolean HasAnimal() {
            return hasAnimal;
        }

        private void AddAnimal(boolean addAnimal) {
            this.hasAnimal = addAnimal;
        }

        private boolean isForEveryWorld() {
            return this.forEveryWorld;
        }
    }

    protected class MutableMeta {
        String target = "";

        private String getTarget() {
            return target;
        }

        private void setTarget(String target) {
            this.target = target;
        }
    }

    public class ChatFormater {
        Meta meta = null;
        List<ChatElement> chatElements;

        private ChatFormater() {
            this.chatElements = new ArrayList<>();
        }

        private void _setMeta(Meta meta) {
            this.meta = meta;
        }

        private void _addChatElement(ChatElement chatElement) {
            this.chatElements.add(chatElement);
        }


        private boolean isGoodPrefix(String message) {
            if (message.toLowerCase().startsWith(meta.getPrefix())) {
                return PREFIX.stream().noneMatch((s) -> (message.toLowerCase().startsWith(s) && s.length() > meta.getPrefix().length()));
            }
            return false;
        }

        private FormatedMessage formatMessage(AsyncPlayerChatEvent event) {
            List<TextComponent> textComponents = new ArrayList<>();
            String message = event.getMessage().trim().substring(meta.getPrefix().length());
            if (message.startsWith(" ")) {
                message = message.substring(1);
            }

            event.setMessage(message);
            MutableMeta mutableMeta = new MutableMeta();
            if (meta.isTargetAdded()) {
                String name = event.getMessage().split(" ")[0];
                mutableMeta.setTarget(name);
                message = event.getMessage().substring(name.length());
                if (message.startsWith(" ")) {
                    message = message.substring(1);
                }
                event.setMessage(message);
            }
            if (meta.HasAnimal()) {

            }
            for (ChatElement chatElement : chatElements) {
                textComponents.add(chatElement.createComponent(event, mutableMeta));
            }
            return new FormatedMessage(meta, mutableMeta, event, textComponents.toArray(new TextComponent[textComponents.size()]));
        }
    }

    public class ChatElement {
        ChatElementType type = ChatElementType.TEXT;
        String text = "";
        ChatColor color = ChatColor.WHITE;
        boolean bold = false;
        boolean italic = false;
        boolean obfuscated = false;
        String commandOnClick;

        private void setType(ChatElementType type) {
            this.type = type;
        }

        private void setText(String text) {
            this.text = text;
        }

        private void setColor(ChatColor color) {
            this.color = color;
        }

        private void setBold(boolean bold) {
            this.bold = bold;
        }

        private void setItalic(boolean italic) {
            this.italic = italic;
        }

        private void setObfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
        }

        private void setCommandOnClick(String commandOnClick) {
            this.commandOnClick = commandOnClick;
        }

        private TextComponent createComponent(AsyncPlayerChatEvent event, MutableMeta mutableMeta) {
            TextComponent textComponent = new TextComponent();
            textComponent.setBold(bold);
            textComponent.setItalic(italic);
            textComponent.setObfuscated(obfuscated);
            if (commandOnClick != null) {
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                        commandOnClick.replace("%t", mutableMeta.getTarget())
                                .replace("%m", event.getMessage())
                                .replace("%a", event.getPlayer().getName()))
                );
            }
            switch (type) {
                case TEXT:
                    textComponent.setText(text);
                    textComponent.setColor(color);
                    break;
                case MESSAGE:
                    textComponent.setText(event.getMessage());
                    textComponent.setColor(color);
                    if (event.getMessage().contains("http://") || event.getMessage().contains("https://")) {
                        int start = event.getMessage().indexOf("http");
                        int end = Math.min(event.getMessage().indexOf(" ", start), event.getMessage().indexOf(")", start));
                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, end != -1 ? event.getMessage().substring(start, end - 1) : event.getMessage().substring(start)));
                    }
                    break;
                case NAME:
                    Player player = event.getPlayer();
                    textComponent.setText(player.getDisplayName());
                    textComponent.setHoverEvent(
                            new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder("{PLAYER-DATA}")
                                            .color(ChatColor.YELLOW)
                                            .create()));

                    break;
                case TARGET:
                    Player target = Main.plugin().getServer().getPlayer(mutableMeta.getTarget());
                    if (target == null) {
                        textComponent.setText(mutableMeta.getTarget());
                    } else {
                        textComponent.setText(target.getDisplayName());
                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(target.getName()).color(ChatColor.YELLOW).create()));
                    }
            }
            return textComponent;
        }
    }

    public enum ChatElementType {
        TEXT, MESSAGE, NAME, TARGET
    }

    protected class FormatedMessage {
        TextComponent[] components;
        AsyncPlayerChatEvent event;
        Meta meta;
        MutableMeta mutableMeta;

        private FormatedMessage(Meta meta, MutableMeta mutableMeta, AsyncPlayerChatEvent event, TextComponent[] components) {
            this.meta = meta;
            this.event = event;
            this.components = components;
            this.mutableMeta = mutableMeta;
        }

        private AsyncPlayerChatEvent getEvent() {
            return event;
        }

        private Meta getMeta() {
            return meta;
        }

        private TextComponent[] getComponents() {
            return components;
        }

        private MutableMeta getMutableMeta() {
            return mutableMeta;
        }

    }

    private static class FormatedMessageSender {
        private static void send(FormatedMessage formatedMessage) {
            AsyncPlayerChatEvent event = formatedMessage.getEvent();
            TextComponent[] arrayMessage = formatedMessage.getComponents();
            TextComponent[] arrayMessageNoItalic = new TextComponent[formatedMessage.getComponents().length];
            Meta meta = formatedMessage.getMeta();
            MutableMeta mutableMeta = formatedMessage.getMutableMeta();
            Player player = event.getPlayer();
            PlayerConfig playerConfig = PlayerConfig.getPlayerConfig(player);
            if (!(Channel.isMJ(player) || playerConfig.isEncamode()) && meta.prefix.equals("$")) {
                for (int i = 0; i < arrayMessage.length; i++) {
                    if (i == 12) {
                        arrayMessage[i].setText(" [requete]>");
                    }
                    if (i != 11) {
                        arrayMessage[i].setColor(ChatColor.LIGHT_PURPLE);
                    }
                }
            }
            if (meta.getRestriction() != null) {
                PlayerConfig pConfig = PlayerConfig.getPlayerConfig(player);
                //  if ("mj".equals(meta.getRestriction()) && !Channel.isMJ(player))
                if ("mj".equals(meta.getRestriction()) && !player.isOp()) {
                    player.sendMessage(ChatColor.RED + meta.getDenialMessage());
                    return;
                }
                if ("enca".equals(meta.getRestriction()) && !(player.isOp() || pConfig.isEncamode())) {
                    player.sendMessage(ChatColor.RED + meta.getDenialMessage());
                    return;
                }
            }
            if (meta.HasAnimal()) {
                if (HasReallyAnAnimal(player)) {
                    String m = NameOfAnimal(player);
                    if (!m.equals("")) {
                        int i = 0;
                        while (!arrayMessage[i].getText().contains(player.getDisplayName())) {
                            i++;
                        }
                        TextComponent t = new TextComponent();
                        t.setText(m);
                        t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Animal de " + player.getName()).color(ChatColor.YELLOW).create()));
                        arrayMessage[i] = t;
                    } else {
                        player.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + "L'animal que vous avez n'a pas de nom.");
                        return;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + "Vous n'avez pas d'animal pour utiliser ce préfixe.");
                    return;
                }
            }
            int rangeSquared = meta.getRange() * meta.getRange();
            if (meta.getRange() < 0) {
                rangeSquared = Integer.MAX_VALUE;
            }
            Location location = player.getLocation();
            for (int i = 0; i < arrayMessage.length; i++) {
                arrayMessageNoItalic[i] = new TextComponent();
                arrayMessageNoItalic[i].setText(formatedMessage.components[i].getText());
                arrayMessageNoItalic[i].setColor(formatedMessage.components[i].getColor());
                arrayMessageNoItalic[i].setHoverEvent(formatedMessage.components[i].getHoverEvent());
                arrayMessageNoItalic[i].setClickEvent(formatedMessage.components[i].getClickEvent());
                arrayMessageNoItalic[i].setItalic(false);
            }
            for (Player p : Main.plugin().getServer().getOnlinePlayers()) {
                if (p.getWorld() == player.getWorld() || meta.isForEveryWorld()) {
                    boolean suitable = true;
                    if (meta.getOnlyFor() != null) {
                        if (p != event.getPlayer()) {
                            // if(("mj".equals(meta.getOnlyFor()) && !Channel.isMJ(p)))
                            if (("mj".equals(meta.getOnlyFor()) && !Channel.isMJ(p))) {
                                suitable = false;
                            }
                            PlayerConfig pConfig = PlayerConfig.getPlayerConfig(p);
                            if ("enca".equals(meta.getOnlyFor()) && !(Channel.isMJ(p) || pConfig.isEncamode())) {
                                suitable = false;
                            }
                            if ("none".equals(meta.getOnlyFor())) {
                                suitable = false;
                            }
                        }
                    }
                    if (meta.isTargetAdded()) {
                        if (p.getName().equals(mutableMeta.getTarget())) {
                            suitable = true;
                        }
                    }
                    if (suitable && (meta.isForEveryWorld() || p.getLocation().distanceSquared(location) < rangeSquared)) {
                        PlayerConfig pconfig = PlayerConfig.getPlayerConfig(p);
                        if (pconfig.isChangechat()) {
                            sendFinalMessage(player, p, arrayMessageNoItalic, playerConfig);
                        } else {
                            sendFinalMessage(player, p, arrayMessage, playerConfig);
                        }
                    }
                }
            }
            String s = Arrays.stream(arrayMessage).map((Object tex) -> ((TextComponent) tex).getText()).collect(Collectors.joining(""));
            Main.log(Level.INFO, s);
        }
    }


    private static void sendFinalMessage(Player sender, Player receiver, TextComponent[] message, PlayerConfig senderConfig) {
        TextComponent[] messageCopied = message.clone();
        String direction = "";

        double x = receiver.getLocation().getX() - sender.getLocation().getX();
        double z = receiver.getLocation().getZ() - sender.getLocation().getZ();
        double norm = Math.sqrt(x * x + z * z);
        if (norm != 0) {
            x /= norm;
            z /= norm;
        }

        String[] names = new String[]{"Ouest", "Nord-Ouest", "Nord", "Nord-Est", "Est", "Sud-Est", "Sud", "Sud-Ouest"};
        double scalarProduct = 0;
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            double xDir = Math.cos(angle);
            double zDir = Math.sin(angle);
            double scalarProductTemp = x * xDir + z * zDir;
            if (scalarProductTemp > scalarProduct) {
                scalarProduct = scalarProductTemp;
                direction = names[i];
            }
        }

        String printedDirection = ChatColor.WHITE + "\nDirection : " + direction;
        if (sender.getUniqueId() == receiver.getUniqueId() || senderConfig.isVanish() || sender.getWorld() != receiver.getWorld()) {
            printedDirection = "";
        }
        for (int i = 0; i < messageCopied.length; i++) {
            // on hover, we replace the hover action containing {PLAYER-DATA} by the relative quadrant direction of the player compared to p
            if (messageCopied[i].getHoverEvent() != null && messageCopied[i].getHoverEvent().getAction().equals(HoverEvent.Action.SHOW_TEXT)) {
                for (BaseComponent baseComponent : messageCopied[i].getHoverEvent().getValue()) {
                    if (baseComponent instanceof TextComponent) {
                        TextComponent textComponent = (TextComponent) baseComponent;

                        if (textComponent.getText().contains("{PLAYER-DATA}")) {
                            //Rebuild from scratch this part without the informations of the class
                            //zuper

                            TextComponent nameWithHover = new TextComponent();
                            nameWithHover.setText(sender.getDisplayName());
                            nameWithHover.setHoverEvent(
                                    new HoverEvent(
                                            HoverEvent.Action.SHOW_TEXT,
                                            new ComponentBuilder(
                                                    sender.getDisplayName()
                                                            + " (" + sender.getName() + ") - Âge : "
                                                            + PlayerInfo.getPlayerInfo(sender).getAge()
                                                            + printedDirection)
                                                    .color(ChatColor.YELLOW)
                                                    .create()
                                    )
                            );


                            messageCopied[i] = nameWithHover;
                        }
                    }
                }
            }
        }

        receiver.spigot().sendMessage(messageCopied);

    }


    public static boolean HasReallyAnAnimal(Player p) {
        boolean hasAnAnimal = false;
        ItemStack item = p.getInventory().getItemInMainHand();
        ItemStack item2 = p.getInventory().getItemInOffHand();
        if (item.getType() != Material.AIR) {
            if (ItemUtil.hasTag(item, "seisan", "animal")) {
                hasAnAnimal = true;
            }
        }
        if (!hasAnAnimal && item2.getType() != Material.AIR) {
            if (ItemUtil.hasTag(item, "seisan", "animal")) {
                hasAnAnimal = true;
            }
        }
        return hasAnAnimal;
    }

    public static String NameOfAnimal(Player p) {
        // && la verif d'après
        String name = "";
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            if (item.getItemMeta() != null) {
                name = item.getItemMeta().getDisplayName();
            }
        } else {
            if (p.getInventory().getItemInOffHand().getItemMeta() != null) {
                name = p.getInventory().getItemInOffHand().getItemMeta().getDisplayName();
            }
        }
        return name;
    }
}
