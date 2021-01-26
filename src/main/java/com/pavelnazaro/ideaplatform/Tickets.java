package com.pavelnazaro.ideaplatform;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Tickets {
    private ArrayList<TicketsInfo> tickets;

    public Tickets() {
        this.tickets = new ArrayList<>();
    }
}
