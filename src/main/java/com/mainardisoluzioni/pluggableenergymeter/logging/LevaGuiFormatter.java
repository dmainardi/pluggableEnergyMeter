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
package com.mainardisoluzioni.pluggableenergymeter.logging;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author Davide Mainardi <davide at mainardisoluzioni.com>
 */
public class LevaGuiFormatter extends Formatter {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        String timestamp = dtf.format(Instant.ofEpochMilli(record.getMillis()).atZone(ZoneId.systemDefault()));
        String level = record.getLevel().getName();
        //String loggerName = record.getLoggerName();
        String message = formatMessage(record);
        String throwable = "";

        if (record.getThrown() != null) {
            StringBuilder sb = new StringBuilder();
            Throwable t = record.getThrown();
            sb.append(System.lineSeparator()).append(t.toString());
            for (StackTraceElement ste : t.getStackTrace()) {
                sb.append(System.lineSeparator()).append("\tat ").append(ste);
            }
            throwable = sb.toString();
        }

        return String.format("%s [%s] %s%s%n", timestamp, level, message, throwable);
    }
}
