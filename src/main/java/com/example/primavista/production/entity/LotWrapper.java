package com.example.primavista.production.entity;

import java.util.List;

public class LotWrapper {

	List<ProductOpers> lots;

	public LotWrapper(List<ProductOpers> lots) {
		super();
		this.lots = lots;
	}

	public List<ProductOpers> getLots() {
		return lots;
	}

	public void setLots(List<ProductOpers> lots) {
		this.lots = lots;
	}
}
