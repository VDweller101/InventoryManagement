package ims.model;

public class InHouse extends Part{

    private int machineId;

    /**
     * Constructor for InHouse Part
     * @param id ID of part.
     * @param name Name of part.
     * @param price Price of part.
     * @param stock Stock level of part.
     * @param min Minimum stock level of part.
     * @param max Maximum stock level of part.
     * @param machineId Machine ID of InHouse part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId)
    {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Get machine ID name of InHouse part.
     * @return Machine ID
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Set machine ID of InHouse Part
     * @param machineId The Machine ID to assign to the part.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
