package com.chromadrive.nickname;

import org.bukkit.entity.Player;

import com.chromadrive.nickname.databases.SQL;

public class Nick
{
    private Player player;
    private SQL database;
    private String uuid;

    public Nick(Nickname plugin, Player player){
        this.player = player;
        database = plugin.getNickDatabase();

        this.uuid = player.getUniqueId().toString();
    }

    public boolean loadNick(){
        String nname = getNick();

        if(nname != null){
            player.setDisplayName(nname);
            return true;
        }

        return false;
    }

    public void unLoadNick(){
        database.removeFromCache(uuid);
        player.setDisplayName(player.getName());
    }

    public void setNick(String nick){
        if(getNick() != null)
        {
            unSetNick();
        }

        database.uploadNick(uuid, nick);
    }

    public void unSetNick(){
        database.deleteNick(uuid);
    }

    public void refreshNick(){
        unLoadNick();
        loadNick();
    }

    private String getNick(){
        return database.downloadNick(uuid);
    }
}