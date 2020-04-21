package app.domain.game;

import java.util.UUID;

class GamePlayer {

    private final UUID id;
    private final Integer tableNumber;
    private final String name;
    private int balance;
    private boolean active;
    private final int INIT_BALANCE = 10000;

    GamePlayer(final String name, final Integer tableNumber) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.tableNumber = tableNumber;
        balance = INIT_BALANCE;
        active = true;
    }

    UUID getId() {
        return id;
    }

    Integer getTableNumber() {
        return tableNumber;
    }

    int getBalance() {
        return balance;
    }

    boolean isActive() {
        return active;
    }

    void updateBalance(int amount) {
        balance = amount;
        checkIfPlayerIsActive();
    }

    void buyIn() {
        balance = INIT_BALANCE;
        activatePlayer();
    }

    String getName() {
        return name;
    }

    void changeState() {
        this.active = !active;
    }

    void deactivatePlayer(){ active = false;} //todo too many methods to change state

    void activatePlayer(){ active = true;}

    void checkIfPlayerIsActive() {
        active = balance > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GamePlayer that = (GamePlayer) o;

        if (balance != that.balance) {
            return false;
        }
        if (active != that.active) {
            return false;
        }
        if (!id.equals(that.id)) {
            return false;
        }
        if (!tableNumber.equals(that.tableNumber)) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + tableNumber.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + balance;
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + INIT_BALANCE;
        return result;
    }
}
