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
import com.digitalpetri.modbus.tcp.client.NettyTcpClientTransport;

/**
 *
 * @author Davide Mainardi <davide at mainardisoluzioni.com>
 */
public class ModbusController {
    
    public static final int TRACE_AND_FOLLOW_ACTUAL_POWER_REGISTER_ADDRESS = 38;

    public static boolean testConnection(String ipAddress, int tcpPort, int modbusUnitId) {
        if (ipAddress != null && !ipAddress.isBlank()) {
            var transport = NettyTcpClientTransport.create(cfg -> {
                cfg.setHostname(ipAddress);
                cfg.setPort(tcpPort);
            });

            var client = ModbusTcpClient.create(transport);
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
}
