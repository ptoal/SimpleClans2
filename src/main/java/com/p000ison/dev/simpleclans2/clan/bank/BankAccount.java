/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 29.10.12 23:52
 */

package com.p000ison.dev.simpleclans2.clan.bank;

/**
 * Represents a BankCommand
 */
public class BankAccount implements Balance {
    private double balance;

    public BankAccount(double balance)
    {
        this.balance = balance;
    }

    @Override
    public double getBalance()
    {
        return balance;
    }

    @Override
    public boolean withdraw(double amount)
    {
        if (amount < 0.0D) {
            throw new IllegalArgumentException("The amount can not be negative if you withdraw something!");
        }
        if (amount > balance) {
            return false;
        }

        balance -= amount;
        System.out.println("Balance: " + amount);
        return true;
    }

    @Override
    public void deposit(double amount)
    {
        if (amount < 0.0D) {
            throw new IllegalArgumentException("The amount can not be negative if you withdraw something!");
        }
        System.out.println("Balance: " + amount);
        balance += amount;
    }

    @Override
    public boolean transfer(double amount, Balance account)
    {
        if (amount > 0.0D) {
            amount = Math.abs(amount);

            if (!this.withdraw(amount)) {
                return false;
            }

            account.deposit(amount);
        } else {
            amount = Math.abs(amount);
            if (!account.withdraw(amount)) {
                return false;
            }

            this.deposit(amount);
        }

        return true;
    }

    public static void main(String[] args)
    {
        BankAccount account1 = new BankAccount(5000.0);
        BankAccount account2 = new BankAccount(1000.0);

        System.out.println(account1.transfer(500, account2));
        System.out.println("Account1: " + account1.getBalance());
        System.out.println("Account2: " + account2.getBalance());
        System.out.println(Double.MAX_VALUE);
    }
}
