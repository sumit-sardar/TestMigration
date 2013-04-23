/**
 * Cache jstree objects. 
 * @author: Santhana R S
 */
function Map() {
    this.items = {};
    this.count = 0;
    this.keys = [];
    this.isDurty = true;
}

Map.prototype.get = function(key) {
    var item = this.items[key];
    var returnVal = null;
    if (item != null) {
        returnVal = item.value;       
    } 
    return returnVal;
};

Map.prototype.put = function(key, value) {
    function map(k, v) {
        if (k!=null){            
        this.key = k;
        this.value = v;
		}
    }
	//Update map
    if (this.items[key] != null){
        this._removeItem(key);
		this._addItem(new map(key, value));
		}
    this._addItem(new map(key, value));
    this.isDurty = true;
};

Map.prototype.remove = function(key) {
	 if(this.get(key) !=null) {
	  	this._removeItem(key);
	  	this.isDurty = true;
	  	return true;
	 } else {
	   return false;
	 }
};

Map.prototype.length = function() {
	return this.count;
};

Map.prototype.clear = function() {
	this.items = {};
	this.count = 0;
};

Map.prototype._addItem = function(item) {
    this.items[item.key] = item;
    this.count++;
	var x = item;
	
};

Map.prototype._removeItem = function(key) {
    var item = this.items[key];
    delete this.items[key];
    this.count--;    
};

Map.prototype.syncIE8Storage = function(key) {
sessionStorage.begin();
sessionStorage.cache = this.items;
sessionStorage.commit();
};
Map.prototype.syncFFStorage = function(key) {
sessionStorage.cache = this.items;
};

