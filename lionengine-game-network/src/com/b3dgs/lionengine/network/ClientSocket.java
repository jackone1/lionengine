/*
 * Copyright (C) 2013 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.b3dgs.lionengine.LionEngineException;

/**
 * Client socket (bridge between server and client).
 */
final class ClientSocket
{
    /** Client socket. */
    private final Socket socket;
    /** Output stream. */
    private final ObjectOutputStream out;
    /** Input stream. */
    private final ObjectInputStream in;
    /** Client id. */
    private final byte clientId;
    /** State. */
    private StateConnection state;
    /** Name. */
    private String name;

    /**
     * Constructor.
     * 
     * @param id The client id.
     * @param socket The socket reference.
     * @param server The server reference.
     */
    ClientSocket(final byte id, final Socket socket, final ServerImpl server)
    {
        clientId = id;
        this.socket = socket;
        name = null;
        try
        {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch (final Exception exception)
        {
            throw new LionEngineException(exception, "Cannot create client");
        }
    }

    /**
     * Set the connection state.
     * 
     * @param state The connection state.
     */
    public void setState(StateConnection state)
    {
        this.state = state;
    }

    /**
     * Get the current connection state.
     * 
     * @return The connection state.
     */
    public StateConnection getState()
    {
        return state;
    }

    /**
     * Set the client name.
     * 
     * @param name The client name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the client name.
     * 
     * @return The client name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Terminate client.
     */
    public void terminate()
    {
        try
        {
            in.close();
        }
        catch (final IOException exception)
        {
            // Ignore
        }
        try
        {
            out.close();
        }
        catch (final IOException exception)
        {
            // Ignore
        }
        try
        {
            socket.close();
        }
        catch (final IOException exception)
        {
            // Ignore
        }
        state = StateConnection.DISCONNECTED;
    }

    /**
     * Receive messages data from the client.
     * 
     * @return The messages data.
     */
    public byte[] receiveMessages()
    {
        try
        {
            final int size = in.available();
            if (size <= 0)
            {
                return null;
            }
            final byte[] data = new byte[size];
            in.readFully(data);
            return data;
        }
        catch (final Exception exception)
        {
            return null;
        }
    }

    /**
     * Send message to the client.
     * 
     * @param data The messages data.
     */
    public void sendMessage(byte data)
    {
        try
        {
            out.write(data);
            out.flush();
        }
        catch (final Exception exception)
        {
            // Ignore
        }
    }

    /**
     * Send message to the client.
     * 
     * @param data The messages data.
     */
    public void sendMessages(byte[] data)
    {
        try
        {
            out.write(data);
            out.flush();
        }
        catch (final Exception exception)
        {
            // Ignore
        }
    }

    /**
     * Get the output stream.
     * 
     * @return The output stream.
     */
    public ObjectOutputStream getOut()
    {
        return out;
    }

    /**
     * Get the input stream.
     * 
     * @return The input stream.
     */
    public ObjectInputStream getIn()
    {
        return in;
    }

    /**
     * Get the client id.
     * 
     * @return The client id.
     */
    public byte getId()
    {
        return clientId;
    }
}