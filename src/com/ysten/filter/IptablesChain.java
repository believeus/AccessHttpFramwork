package com.ysten.filter;

import java.util.ArrayList;
import java.util.List;

public class IptablesChain implements IptablesFilter {
	
	private List<IptablesFilter> iptablesFilters = new ArrayList<IptablesFilter>();

	public IptablesChain addFilter(IptablesFilter filter) {
		this.iptablesFilters.add(filter);
		return this;
	}

	@Override
	public void doFilter() throws Exception {
		for (IptablesFilter iptablesFilter : iptablesFilters) {
			iptablesFilter.doFilter();
		}
	}

}
