package core.net.packets;

import java.util.logging.Level;

import com.sun.istack.internal.logging.Logger;

import core.game.model.entity.player.Player;
import core.net.packets.incoming.AttackPlayer;
import core.net.packets.incoming.Bank10;
import core.net.packets.incoming.Bank5;
import core.net.packets.incoming.BankAll;
import core.net.packets.incoming.BankX1;
import core.net.packets.incoming.BankX2;
import core.net.packets.incoming.ChallengePlayer;
import core.net.packets.incoming.ChangeAppearance;
import core.net.packets.incoming.ChangeRegions;
import core.net.packets.incoming.Chat;
import core.net.packets.incoming.ClickItem;
import core.net.packets.incoming.ClickNPC;
import core.net.packets.incoming.ClickObject;
import core.net.packets.incoming.ActionButtons;
import core.net.packets.incoming.ClickingInGame;
import core.net.packets.incoming.ClickingAction;
import core.net.packets.incoming.CommandPacket;
import core.net.packets.incoming.DialoguePacket;
import core.net.packets.incoming.DropItem;
import core.net.packets.incoming.FollowPlayer;
import core.net.packets.incoming.IdleLogout;
import core.net.packets.incoming.ItemClick2;
import core.net.packets.incoming.ItemClick3;
import core.net.packets.incoming.ItemOnGroundItem;
import core.net.packets.incoming.ItemOnItem;
import core.net.packets.incoming.ItemOnNpc;
import core.net.packets.incoming.ItemOnObject;
import core.net.packets.incoming.MagicOnFloorItems;
import core.net.packets.incoming.MagicOnItems;
import core.net.packets.incoming.MoveItems;
import core.net.packets.incoming.PickupItem;
import core.net.packets.incoming.PrivateMessaging;
import core.net.packets.incoming.RemoveItem;
import core.net.packets.incoming.Report;
import core.net.packets.incoming.SilentPacket;
import core.net.packets.incoming.Trade;
import core.net.packets.incoming.Walking;
import core.net.packets.incoming.WearItem;


public class PacketHandler{
	
	public static final Logger logger = Logger.getLogger(PacketHandler.class);

	private static PacketType packetId[] = new PacketType[256];
	
	static {		
		SilentPacket u = new SilentPacket();
		packetId[PacketConstants.FOCUS_CHANGE] = u; //Sent when the game client window goes in and out of focus.
		packetId[PacketConstants.IDLE_LOGOUT] = u;
		packetId[77] = u; // player saving? it's called every 1-2 minutes
		packetId[PacketConstants.CAMERA_MOVEMENT] = u;
		packetId[78] = u; // ?
		packetId[36] = u; // ?
		packetId[226] = u; // ?
		packetId[246] = u;
		packetId[148] = u;
		packetId[183] = u;
		packetId[230] = u;
		packetId[136] = u;
		packetId[189] = u;
		packetId[152] = u;
		packetId[200] = u;
		packetId[85] = u;
		packetId[165] = u;
		packetId[238] = u;
		packetId[150] = u;
		packetId[218] = new Report();
		packetId[40] = new DialoguePacket();
		ClickObject co = new ClickObject();
		packetId[132] = co;
		packetId[252] = co;
		packetId[70] = co;
		packetId[57] = new ItemOnNpc();
		ClickNPC cn = new ClickNPC();
		packetId[PacketConstants.ATTACK_NPC] = cn;
		packetId[PacketConstants.MAGIC_ON_NPC] = cn;
		packetId[PacketConstants.NPC_ACTION_1] = cn;
		packetId[PacketConstants.NPC_ACTION_2] = cn;
		packetId[PacketConstants.NPC_ACTION_3] = cn;
		packetId[16] = new ItemClick2();		
		packetId[75] = new ItemClick3();	
		packetId[122] = new ClickItem();
		packetId[241] = new ClickingInGame();
		packetId[4] = new Chat();
		packetId[236] = new PickupItem();
		packetId[87] = new DropItem();
		packetId[185] = new ActionButtons();
		packetId[130] = new ClickingAction();
		packetId[PacketConstants.PLAYER_COMMAND] = new CommandPacket();
		packetId[214] = new MoveItems();
		packetId[237] = new MagicOnItems();
		packetId[181] = new MagicOnFloorItems();
		packetId[202] = new IdleLogout();
		AttackPlayer ap = new AttackPlayer();
		packetId[73] = ap;
		packetId[249] = ap;
		packetId[128] = new ChallengePlayer();
		packetId[39] = new Trade();
		packetId[139] = new FollowPlayer();
		packetId[41] = new WearItem();
		packetId[145] = new RemoveItem();
		packetId[117] = new Bank5();
		packetId[43] = new Bank10();
		packetId[129] = new BankAll();
		packetId[101] = new ChangeAppearance();
		PrivateMessaging pm = new PrivateMessaging();
		packetId[PacketConstants.ADD_FRIEND] = pm;
		packetId[PacketConstants.PRIVATE_MESSAGE] = pm;
		packetId[PacketConstants.REMOVE_FRIEND] = pm;
		packetId[59] = pm; // ?
		packetId[PacketConstants.PRIVACY_OPTIONS] = pm;
		packetId[PacketConstants.ADD_IGNORE] = pm;
		packetId[PacketConstants.BANK_X_PART_1] = new BankX1(); // Sent when a player requests to bank an X amount of items.
		packetId[PacketConstants.BANK_X_PART_2] = new BankX2(); // Sent when a player enters an X amount of items they want to bank.
		Walking w = new Walking();
		packetId[PacketConstants.WALK_ON_COMMAND] = w;
		packetId[PacketConstants.REGULAR_WALK] = w;
		packetId[248] = w;
		packetId[PacketConstants.ITEM_ON_ITEM] = new ItemOnItem();
		packetId[PacketConstants.ITEM_ON_OBJECT] = new ItemOnObject();
		packetId[25] = new ItemOnGroundItem();
		ChangeRegions cr = new ChangeRegions();
		packetId[PacketConstants.LOADED_REGION] = cr; // Sent when the client finishes loading a map region.
		packetId[PacketConstants.ENTER_REGION] = cr; // Sent when a player enters a new map region.
		//packetId[60] = new ClanChat();
	}


	public static void processPacket(Player c, int packetType, int packetSize) {
        PacketType p = packetId[packetType];
        if(p != null && packetType > 0 && packetType < 257 && packetType == c.packetType && packetSize == c.packetSize) {
//            if (Configuration.SERVER_DEBUG && c.getRights().equals(Rights.DEVELOPER)) {
//                c.sendMessage("PacketType: " + packetType + ". PacketSize: " + packetSize + ".");
//            }
            try {
                p.processPacket(c, packetType, packetSize);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            c.disconnected = true;
            logger.log(Level.SEVERE, c.playerName + "is sending invalid PacketType: " + packetType + ". PacketSize: " + packetSize);
        }
    }
}
