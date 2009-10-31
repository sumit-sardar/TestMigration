package dto; 


/**
 *@author Tata Consultancy Services
 * Level class have the framework details.Which presents Framework UI. 
 */
public class Level implements java.io.Serializable
{ 
    static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private Integer order;
    private Boolean deletable;
    private Boolean beforeInsertable;
    private Boolean afterInsertable;
    
    
    public Level() {
        this.id = new Integer(0);
        this.name = "";
        this.order = new Integer(0);
        this.deletable = Boolean.TRUE;
        this.beforeInsertable = Boolean.TRUE;
        this.afterInsertable = Boolean.TRUE;
    }

    public Level(Integer id, String name, Integer order) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.deletable = Boolean.TRUE;
        this.beforeInsertable = Boolean.TRUE;
        this.afterInsertable = Boolean.TRUE;
    }

    public Level(Integer id, String name, Integer order, Boolean deletable,
                 Boolean beforeInsertable, Boolean afterInsertable) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.deletable = deletable;
        this.beforeInsertable = beforeInsertable;
        this.afterInsertable = afterInsertable;
    }
    
	public Integer getId() {
		return this.id != null ? this.id : new Integer(0);
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return this.name != null ? this.name : "";
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOrder() {
		return this.order != null ? this.order : new Integer(0);
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Boolean getDeletable() {
		return this.deletable;
	}
	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
	}
	public Boolean getBeforeInsertable() {
		return this.beforeInsertable;
	}
	public void setBeforeInsertable(Boolean beforeInsertable) {
		this.beforeInsertable = beforeInsertable;
	}
	public Boolean getAfterInsertable() {
		return this.afterInsertable;
	}
	public void setAfterInsertable(Boolean afterInsertable) {
		this.afterInsertable = afterInsertable;
	}
} 