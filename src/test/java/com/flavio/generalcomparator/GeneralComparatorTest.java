package com.flavio.generalcomparator;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GeneralComparatorTest {
    @Test
    public void verifica_se_inteiros_sao_iguais_valor(){
        assertTrue(GeneralComparator.compare(1, 1));
    }

    @Test
    public void verifica_se_inteiros_sao_diferentes_valor(){
        assertFalse(GeneralComparator.compare(2, 5));
    }

    @Test
    public void verifica_se_inteiro_diferente_de_long(){
        assertFalse(GeneralComparator.compare(2, 2L));
    }

    @Test
    public void verifica_se_lista_igual_lista_valores(){
        assertTrue(GeneralComparator.compare(Arrays.asList(1, "a", 30.4), Arrays.asList(1, "a", 30.4)));
    }

    @Test
    public void verifica_se_lista_igual_lista_diferentes_valores(){
        assertFalse(GeneralComparator.compare(Arrays.asList(1, "31efnjdfn", "a"), Arrays.asList(1, "a", 30.4)));
    }
}
