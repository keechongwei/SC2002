public class Medication {
	private String _medicationName;
	private int _stock;
	private boolean _lowStockAlert;
	private int _lowStockValue;

	public Medication(String _medicationName, int _stock, int _lowStockValue) {
		this._medicationName = _medicationName;
		this._stock = _stock;
		this._lowStockValue = _lowStockValue;
		this._lowStockAlert = _stock <= _lowStockValue;
	}

	public String getMedicationName() {
		return _medicationName;
	}

	public int getStock() {
		return _stock;
	}

	public boolean isLowStockAlert() {
		return _lowStockAlert;
	}

	public int get_LowStockValue () {
		return _lowStockValue;
	}

	public void setlowStockAlert(boolean alert) {
		this._lowStockAlert = alert;
	}

	public void addStock(int amount) {
		_stock += amount;
		if (_stock > _lowStockValue) {
			_lowStockAlert = false;
		}
	}

	public void removeStock(int amount) {
		if (_stock >= amount) {
			_stock -= amount;
			_lowStockAlert = _stock <= _lowStockValue;
			return true;
		} else {
			return false;
			System.out.println("Not enough stock!");
		}
	}

	public void updatelowStockLevelLine(int newLimit) {
		_lowStockValue = newLimit;
		_lowStockAlert = _stock <= _lowStockValue;
	}
}