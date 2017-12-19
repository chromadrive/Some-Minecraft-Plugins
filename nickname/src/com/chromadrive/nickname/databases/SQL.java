package com.chromadrive.nickname.databases;

import org.bukkit.configuration.ConfigurationSection;

import com.chromadrive.nickname.Nickname;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class SQL
{
    private Connection connection;
    protected Nickname plugin;
    private HashMap<String, String> cache = new HashMap<String, String>();

    public SQL( Nickname plugin )
    {
        this.plugin = plugin;

        plugin.getServer().getScheduler().runTaskTimerAsynchronously( plugin, new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if( connection != null && ! connection.isClosed() )
                    {
                        connection.createStatement().execute( "/* ping */ SELECT 1" );
                    }
                }
                catch( SQLException e )
                {
                    connection = getNewConnection();
                }
            }
        }, 60 * 20, 60 * 20 );
    }

    protected abstract Connection getNewConnection();

    protected abstract String getName();

    public boolean query( String sql ) throws SQLException
    {
        return connection.createStatement().execute( sql );
    }

    public String getConfigName()
    {
        return getName().toLowerCase().replace(" ", "");
    }

    public ConfigurationSection getConfigSection()
    {
        return plugin.getConfig().getConfigurationSection(getConfigName());
    }

    public boolean checkConnection()
    {
        try
        {
            if( connection == null || connection.isClosed() )
            {
                connection = getNewConnection();

                if( connection == null || connection.isClosed() )
                {
                    return false;
                }

                query( "CREATE TABLE IF NOT EXISTS nickname (uuid varchar(36) NOT NULL, nick varchar(64) NOT NULL, PRIMARY KEY (uuid))" );
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public String downloadNick( String uuid )
    {
        String nick = null;

        if( cache.containsKey( uuid ) )
        {
            nick = cache.get( uuid );
        }
        else
        {
            if( !checkConnection() )
            {
                plugin.log( "Error with database" );
                return null;
            }
            PreparedStatement statement;
            try
            {
                statement = connection.prepareStatement( "SELECT nick FROM nickname WHERE uuid = '" + uuid + "';" );

                ResultSet set = statement.executeQuery();

                while( set.next() )
                {
                    nick = set.getString( "nick" );

                    cache.put( uuid, nick );
                }
            }
            catch( SQLException e )
            {
                e.printStackTrace();
            }
        }

        return nick;
    }

    public void removeFromCache( String uuid )
    {
        if( cache.containsKey( uuid ) )
        {
            cache.remove( uuid );
        }
    }

    public void uploadNick( String uuid, String nick )
    {
        if( ! checkConnection() )
        {
            plugin.log( "Error with database" );
            return;
        }

        if( downloadNick( uuid ) != null )
        {
            deleteNick( uuid );
        }

        PreparedStatement statement;
        try
        {
            statement = connection.prepareStatement( "INSERT INTO nickname (uuid, nick) VALUES ('" + uuid + "','" + nick + "');" );

            statement.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteNick( String uuid )
    {
        if( ! checkConnection() )
        {
            plugin.log( "Error with database" );
            return;
        }

        PreparedStatement statement;
        try
        {
            statement = connection.prepareStatement( "DELETE FROM nickname WHERE uuid = '" + uuid + "';" );

            statement.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}