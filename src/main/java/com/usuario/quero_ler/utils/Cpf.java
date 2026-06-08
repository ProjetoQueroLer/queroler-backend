package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.exceptions.especies.CpfInvalidoException;

public final class Cpf {
    private Cpf() {
    }

    public static boolean isValid(String cpf) {
        if (cpf == null)
            return false;

        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11)
            return false;

        if (digits.matches("(\\d)\\1{10}"))
            return false;

        try {
            int[] nums = new int[11];
            for (int i = 0; i < 11; i++)
                nums[i] = digits.charAt(i) - '0';

            int sum = 0;
            for (int i = 0; i < 9; i++)
                sum += nums[i] * (10 - i);
            int rem = sum % 11;
            int check1 = (rem < 2) ? 0 : 11 - rem;
            if (nums[9] != check1)
                return false;

            sum = 0;
            for (int i = 0; i < 10; i++)
                sum += nums[i] * (11 - i);
            rem = sum % 11;
            int check2 = (rem < 2) ? 0 : 11 - rem;
            return nums[10] == check2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String normalize(String cpf) {
        if (cpf == null)
            return null;
        return cpf.replaceAll("\\D", "");
    }

    public static void validateOrThrow(String cpf) {
        if (!isValid(cpf))
            throw new CpfInvalidoException();
    }
}
