package stock.master.app.service;

public interface crawlerInterface {

	/**
	 * remove knowledge by id
	 *
	 * @param knowledegId
	 * 
	 */
	void crawlFinanceInfo(String sid);
	
	void crawlLargeHolderInfo(String sid);
	
	void crawlLegalInfo(String sid);
	
	void crawlTechInfo(String sid);
}
