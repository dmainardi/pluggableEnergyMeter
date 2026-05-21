/*
 * Copyright (C) 2026 Mainardi Davide <daivde at mainardisoluzioni.com>.
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
package com.mainardisoluzioni.pluggableenergymeter.energy;

import java.math.BigDecimal;

/**
 *
 * @author Mainardi Davide <daivde at mainardisoluzioni.com>
 */
public class EnergyInfo {
    private Integer actualPower;
    private BigDecimal energyConsumption;
    private BigDecimal cumulativeEnergyConsumption;

    public EnergyInfo(Integer actualPower, BigDecimal energyConsumption, BigDecimal cumulativeEnergyConsumption) {
        this.actualPower = actualPower;
        this.energyConsumption = energyConsumption;
        this.cumulativeEnergyConsumption = cumulativeEnergyConsumption;
    }

    public Integer getActualPower() {
        return actualPower;
    }

    public void setActualPower(Integer actualPower) {
        this.actualPower = actualPower;
    }

    public BigDecimal getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(BigDecimal energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public BigDecimal getCumulativeEnergyConsumption() {
        return cumulativeEnergyConsumption;
    }

    public void setCumulativeEnergyConsumption(BigDecimal cumulativeEnergyConsumption) {
        this.cumulativeEnergyConsumption = cumulativeEnergyConsumption;
    }
    
}
