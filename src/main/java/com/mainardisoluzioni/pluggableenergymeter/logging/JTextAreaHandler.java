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

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 *
 * @author Davide Mainardi <davide at mainardisoluzioni.com>
 */
public class JTextAreaHandler extends StreamHandler {

        private final javax.swing.JTextArea textArea;

        public JTextAreaHandler(javax.swing.JTextArea textArea, Formatter formatter) {
            this.textArea = textArea;
            if (formatter != null) {
                setFormatter(formatter);
            }
        }

        @Override
        public void publish(LogRecord record) {
            if (getFilter() == null || getFilter().isLoggable(record)) {
                textArea.append(getFormatter().format(record));
            }
        }

        @Override
        public void flush() {
            // Flush any buffered output
            super.flush();
        }

        @Override
        public void close() throws SecurityException {
            // Clean up resources
            super.close();
        }

    }