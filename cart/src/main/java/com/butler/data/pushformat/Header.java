/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.data.pushformat;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author arsh
 */
public class Header {
    private List<String> registration_ids = Arrays.asList("eaVrt4aR-Yo:APA91bGA29Jre26gaI3NZxOFxx0CYpQ7FzSns4pITEPtd6r1VGJQHbBlzC7I3M78R6D6tnMhvH7Skv00xBmbdTj0lpd9efVRUrBtOBs0z2Z0OiG6fkKJrtE4pk77Hm73KYjUzmQaA9zl", "fr_hZx3aW8M:APA91bHv6M6SjCnLkcmex0_capQWELhpCeoB3bpeda4hncbnFIInJE05YHopu4GHSCDf7vAaaPNwN7Fr5MRq1nVG-NQhozFCpl_33bS0unN1CJFvVmDkmOyTsb0Za5zMOzMhh4fDTta_", "dV2gx6cA0xA:APA91bH6z2qfd2tjlrjWThPytxC6Y-1sRJwoTfLVYREaXvIg1mtruaC7XdYMjz3e2Al_Wta8m8yWpfKuGIVcSgV_uGh21bP2r0Gp6xy_PaEDudTQctuZ-RgzWhWZjjEhlYKxPdbgCGHH");
            private Data data = new Data();

    /**
     * @return the registration_ids
     */
    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    /**
     * @return the data
     */
    public Data getData() {
        return data;
    }
}
