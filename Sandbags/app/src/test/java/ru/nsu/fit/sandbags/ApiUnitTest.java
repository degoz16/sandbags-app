package ru.nsu.fit.sandbags;

import org.junit.Test;

import java.util.List;

import ru.nsu.fit.sandbags.api.ServerAPI;

public class ApiUnitTest {
    @Test
    public void serverGetStateTest() {
        ServerAPI serverAPI = new ServerAPI();
        List<List<Integer>> state = serverAPI.getCurrentSandbagsState();
        System.out.println(state.size());
        for (List<Integer> place : state) {
            for (Integer emptyBags : place) {
                System.out.println(emptyBags);
            }
        }
    }
}
