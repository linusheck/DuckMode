package me.glatteis.duckmode.reflection;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class DuckReflectionMethods {


    public static void actionbar(Player p, String label) {
        try {
            //Reflection formatting:

            //Needed Classes
            Class<?> iChatBaseComponentClass = DuckReflection.getNMSClass("IChatBaseComponent"); //$NON-NLS-1$
            Class<?> packetPlayOutChat = DuckReflection.getNMSClass("PacketPlayOutChat"); //$NON-NLS-1$
            Class<?> chatSerializer = DuckReflection.getNMSClass("IChatBaseComponent$ChatSerializer"); //$NON-NLS-1$

            //Get Packet
            Method a = chatSerializer.getDeclaredMethod("a", String.class); //$NON-NLS-1$
            Object iChatBaseComponent = iChatBaseComponentClass.cast(a.invoke(chatSerializer, "{\"text\": \"" + label + "\"}")); //$NON-NLS-1$ //$NON-NLS-2$
            Constructor<?> packetPlayOutChatConstructor = packetPlayOutChat.getConstructor(iChatBaseComponentClass, byte.class);
            Object bar = packetPlayOutChatConstructor.newInstance(iChatBaseComponent, (byte) 2);

            //Get Connection & Send
            Object connection = DuckReflection.getConnection(p);
            DuckReflection.sendPacket(connection, bar);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void title(Player p, String label, int in, int stay, int out) {

        try {
            Class<?> iChatBaseComponent = DuckReflection.getNMSClass("IChatBaseComponent"); //$NON-NLS-1$
            Class<?> packetPlayOutTitle = DuckReflection.getNMSClass("PacketPlayOutTitle"); //$NON-NLS-1$
            Class<?> enumTitleAction = DuckReflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction"); //$NON-NLS-1$
            Class<?> chatSerializer = DuckReflection.getNMSClass("IChatBaseComponent$ChatSerializer"); //$NON-NLS-1$

            Method m1 = chatSerializer.getMethod("a", String.class); //$NON-NLS-1$
            Object baseComponent = m1.invoke(null, "{\"text\": \"" + label + "\"}"); //$NON-NLS-1$ //$NON-NLS-2$

            Constructor<?> c1 = packetPlayOutTitle.getConstructor(enumTitleAction, iChatBaseComponent);
            Object title = c1.newInstance(enumTitleAction.getEnumConstants()[0], baseComponent);
            Object connection = DuckReflection.getConnection(p);
            DuckReflection.sendPacket(connection, title);

            Constructor<?> c2 = packetPlayOutTitle.getConstructor(enumTitleAction, iChatBaseComponent, int.class, int.class, int.class);
            Object times = c2.newInstance(enumTitleAction.getEnumConstants()[2], baseComponent, in, stay, out);

            DuckReflection.sendPacket(connection, times);

        } catch (Exception e) {
            e.printStackTrace();
        }

//		IChatBaseComponent title_text = ChatSerializer.a("{\"text\": \"" + label +  "\"}");
//        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, title_text);
//        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(title);
//        IChatBaseComponent times = ChatSerializer.a("");
//        PacketPlayOutTitle length = new PacketPlayOutTitle(EnumTitleAction.TIMES, times, in, stay, out);
//		((CraftPlayer)p).getHandle().playerConnection.sendPacket(length);
    }

    public static void subtitle(Player p, String label, int in, int stay, int out) {
        try {
            Class<?> iChatBaseComponent = DuckReflection.getNMSClass("IChatBaseComponent"); //$NON-NLS-1$
            Class<?> packetPlayOutTitle = DuckReflection.getNMSClass("PacketPlayOutTitle"); //$NON-NLS-1$
            Class<?> enumTitleAction = DuckReflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction"); //$NON-NLS-1$
            Class<?> chatSerializer = DuckReflection.getNMSClass("IChatBaseComponent$ChatSerializer"); //$NON-NLS-1$

            Method m1 = chatSerializer.getMethod("a", String.class); //$NON-NLS-1$
            Object baseComponent = m1.invoke(null, "{\"text\": \"" + label + "\"}"); //$NON-NLS-1$ //$NON-NLS-2$

            Constructor<?> c1 = packetPlayOutTitle.getConstructor(enumTitleAction, iChatBaseComponent);
            Object title = c1.newInstance(enumTitleAction.getEnumConstants()[1], baseComponent);
            Object connection = DuckReflection.getConnection(p);
            DuckReflection.sendPacket(connection, title);

            Constructor<?> c2 = packetPlayOutTitle.getConstructor(enumTitleAction, iChatBaseComponent, int.class, int.class, int.class);
            Object times = c2.newInstance(enumTitleAction.getEnumConstants()[2], baseComponent, in, stay, out);

            DuckReflection.sendPacket(connection, times);

        } catch (Exception e) {
            e.printStackTrace();
        }

//		IChatBaseComponent title_text = ChatSerializer.a("{\"text\": \"" + label +  "\"}");
//        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, title_text);
//        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(title);
//        IChatBaseComponent times = ChatSerializer.a("");
//        PacketPlayOutTitle length = new PacketPlayOutTitle(EnumTitleAction.TIMES, times, in, stay, out);
//		((CraftPlayer)p).getHandle().playerConnection.sendPacket(length);
    }

}
