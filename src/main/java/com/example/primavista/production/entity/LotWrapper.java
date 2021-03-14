package com.example.primavista.production.entity;

import java.util.List;

public class LotWrapper {

	List<Lot> lots;

	public LotWrapper(List<Lot> lots) {
		super();
		this.lots = lots;
	}

	public List<Lot> getLots() {
		return lots;
	}

	public void setLots(List<Lot> lots) {
		this.lots = lots;
	}
}
