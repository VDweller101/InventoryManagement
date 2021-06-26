package ims.model;

public class Outsourced extends Part{

    private String companyName;

    /**
     * Constructor for Outsourced Part.
     * @param id ID of part.
     * @param name Name of part.
     * @param price Price of part.
     * @param stock Stock level of part.
     * @param min Minimum stock level of part.
     * @param max Maximum stock level of part.
     * @param companyName Company name of outsourced part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName)
    {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Get company name of outsourced part.
     * @return Company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set company name of outsourced part.
     * @param companyName The company name to assign to the part.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
