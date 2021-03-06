package com.jamesshore.finances.ui;

import java.io.*;
import com.jamesshore.finances.domain.*;
import com.jamesshore.finances.persistence.*;
import com.jamesshore.finances.persistence.UserConfiguration.Observer;
import com.jamesshore.finances.values.*;

public class ApplicationModel implements Observer {

	public static final Year DEFAULT_STARTING_YEAR = new Year(2010);
	public static final Year DEFAULT_ENDING_YEAR = new Year(2050);
	public static final GrowthRate DEFAULT_GROWTH_RATE = new GrowthRate(10);
	public static final TaxRate DEFAULT_CAPITAL_GAINS_TAX_RATE = new TaxRate(25);

	private Year startingYear = DEFAULT_STARTING_YEAR;
	private Year endingYear = DEFAULT_ENDING_YEAR;
	private GrowthRate growthRate = DEFAULT_GROWTH_RATE;
	private TaxRate capitalGainsTaxRate = DEFAULT_CAPITAL_GAINS_TAX_RATE;

	private UserConfiguration configuration;
	private StockMarketTableModel stockMarketTableModel;

	public ApplicationModel() {
		this.configuration = new UserConfiguration();
		this.configuration.setObserver(this);
		this.stockMarketTableModel = new StockMarketTableModel(stockMarketProjection());
	}

	public UserConfiguration userConfiguration() {
		return configuration;
	}

	public StockMarketTableModel stockMarketTableModel() {
		return stockMarketTableModel;
	}

	@Override
	public void configurationUpdated() {
		stockMarketTableModel.setProjection(stockMarketProjection());
	}

	public StockMarketProjection stockMarketProjection() {
		StockMarketYear firstYear = new StockMarketYear(startingYear, configuration.getStartingBalance(), configuration.getStartingCostBasis(), growthRate, capitalGainsTaxRate);
		return new StockMarketProjection(firstYear, endingYear, configuration.getYearlySpending());
	}

	public void save(File path) throws IOException {
		configuration.save(path);
	}

	public File lastSavedPathOrNullIfNeverSaved() {
		return configuration.lastSavedPathOrNullIfNeverSaved();
	}
}
