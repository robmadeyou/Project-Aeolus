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
import core.net.packets.incoming.CloseInterface;
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
		packetId[PacketConstants.FOCUS_CHANGE] = u;
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
		packetId[PacketConstants.CHAT] = new Chat();
		packetId[PacketConstants.PICKUP_GROUND_ITEM] = new PickupItem();
		packetId[PacketConstants.DROP_ITEM] = new DropItem();
		packetId[PacketConstants.BUTTON_CLICK] = new ActionButtons();
		packetId[PacketConstants.CLOSE_WINDOW] = new CloseInterface();
		packetId[PacketConstants.PLAYER_COMMAND] = new CommandPacket();
		packetId[PacketConstants.MOVE_ITEM] = new MoveItems();
		packetId[PacketConstants.MAGIC_ON_ITEMS] = new MagicOnItems();
		packetId[181] = new MagicOnFloorItems();
		packetId[202] = new IdleLogout();
		AttackPlayer ap = new AttackPlayer();
		packetId[PacketConstants.ATTACK_PLAYER] = ap; //73
		packetId[PacketConstants.MAGIC_ON_PLAYER] = ap;
		packetId[PacketConstants.CHALLENGE_PLAYER] = new ChallengePlayer();
		packetId[PacketConstants.TRADE_REQUEST] = new Trade();
		packetId[PacketConstants.FOLLOW_PLAYER] = new FollowPlayer();
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
		packetId[PacketConstants.BANK_X_PART_1] = new BankX1();
		packetId[PacketConstants.BANK_X_PART_2] = new BankX2();
		Walking w = new Walking();
		packetId[PacketConstants.WALK_ON_COMMAND] = w;
		packetId[PacketConstants.REGULAR_WALK] = w;
		packetId[PacketConstants.MAP_WALK] = w;
		packetId[PacketConstants.ITEM_ON_ITEM] = new ItemOnItem();
		packetId[PacketConstants.ITEM_ON_OBJECT] = new ItemOnObject();
		packetId[PacketConstants.ITEM_ON_GROUND_ITEM] = new ItemOnGroundItem();
		ChangeRegions cr = new ChangeRegions();
		packetId[PacketConstants.LOADED_REGION] = cr;
		packetId[PacketConstants.ENTER_REGION] = cr;
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
