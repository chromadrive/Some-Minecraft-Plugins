package com.chromadrive.nickname.databases;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import com.chromadrive.nickname.Nickname;

public class SQLite extends SQL
{
    private final Nickname plugin;

    public SQLite( Nickname plugin )
    {
        super(plugin);

        this.plugin = plugin;
    }

    protected Connection getNewConnection()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");

            return DriverManager.getConnection( "jdbc:sqlite:" + new File( plugin.getDataFolder(), "nicknames.db" ).getAbsolutePath() );
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String getName()
    {
        return "SQLite";
    }
}
