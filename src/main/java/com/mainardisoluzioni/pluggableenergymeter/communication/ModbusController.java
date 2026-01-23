/*
 * Copyright (C) 2026 Davide Mainardi <davide at mainardisoluzioni.com>.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mainardisoluzioni.pluggableenergymeter.communication;

import com.digitalpetri.modbus.client.ModbusTcpClient;
import com.digitalpetri.modbus.exceptions.ModbusExecutionException;
import com.digitalpetri.modbus.exceptions.ModbusResponseException;
import com.digitalpetri.modbus.exceptions.ModbusTimeoutException;
import com.digitalpetri.modbus.pdu.ReadInputRegistersRequest;
import com.digitalpetri.modbus.pdu.ReadInputRegistersResponse;
import com.digitalpetri.modbus.tcp.client.NettyTcpClientTransport;
import java.util.Optional;

/**
 *
 * @author Davide Mainardi <davide at mainardisoluzioni.com>
 */
public class ModbusController {
    
    public static final int TRACE_AND_FOLLOW_ACTUAL_POWER_REGISTER_ADDRESS = 38;
    
    private ModbusTcpClient client;
    
    private static Optional<ModbusTcpClient> createClient(String ipAddress, int tcpPort) {
        if (ipAddress != null && !ipAddress.isBlank()) {
            var transport = NettyTcpClientTransport.create(cfg -> {
                cfg.setHostname(ipAddress);
                cfg.setPort(tcpPort);
            });

            return Optional.of(ModbusTcpClient.create(transport));
        }
        return Optional.empty();
    }

    public static boolean testConnection(String ipAddress, int tcpPort, int modbusUnitId) {
        var client = createClient(ipAddress, tcpPort).orElse(null);
        if (client != null) {
            try {
                client.connect();

                client.readInputRegisters(
                        modbusUnitId,
                        new ReadInputRegistersRequest(
                                TRACE_AND_FOLLOW_ACTUAL_POWER_REGISTER_ADDRESS,
                                1
                        )
                );

                client.disconnect();
                return true;
            } catch (ModbusExecutionException | ModbusResponseException | ModbusTimeoutException e) {
                // NOOP
            }
        }
        return false;
    }
    
    public Integer readInstantaneousPower(String ipAddress, int tcpPort, int modbusUnitId) {
        if (client == null)
            client = createClient(ipAddress, tcpPort).orElse(null);
        if (client != null) {
            try {
                if (!client.isConnected())
                    client.connect();

                ReadInputRegistersResponse readInputRegisters = client.readInputRegisters(
                        modbusUnitId,
                        new ReadInputRegistersRequest(
                                TRACE_AND_FOLLOW_ACTUAL_POWER_REGISTER_ADDRESS,
                                1
                        )
                );

                return convertRegisterValue(readInputRegisters.registers());
            } catch (ModbusExecutionException | ModbusResponseException | ModbusTimeoutException e) {
                // NOOP
            }
        }
        
        return -1;
    }
    
    public void disconnect() {
        try {
            if (client != null && client.isConnected())
                client.disconnect();
        } catch (ModbusExecutionException ex) {
            // NOOP
        }
    }
    
    /**
     * See https://www.baeldung.com/java-byte-array-to-number#1-byte-array-to-int-and-long
     * @param readInputRegisters
     * @return 
     */
    private int convertRegisterValue(byte[] readInputRegisters) {
        int value = 0;
        for (byte b : readInputRegisters)
            value = (value << 8) + (b & 0xFF);
        
        return value;
    }
}
