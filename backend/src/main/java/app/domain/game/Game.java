package app.domain.game;

import app.domain.game.exceptions.GameIsFull;
import app.domain.game.exceptions.GamePlayerNotFound;
import app.domain.player.GamePlayer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Game {

    private Deque<GamePlayer> gamePlayers;
    private final Blinds blinds;
    private long gameTimeStamp;
    private int entryFee;

    Game() {
        gamePlayers = new ArrayDeque<>();
        blinds = new Blinds();
    }

    UUID addPlayer(GamePlayer gamePlayer) {
        if (gamePlayers.isEmpty()) {
            gamePlayers.add(gamePlayer);
        } else {
            int tableNumberOfCurrentPlayer = gamePlayers.getFirst().getTableNumber();
            int tableNumberAfterNewOne = getTableNumberAfter(gamePlayer.getTableNumber());
            movePlayersInReferenceTo(tableNumberAfterNewOne);
            gamePlayers.addFirst(gamePlayer);
            movePlayersInReferenceTo(tableNumberOfCurrentPlayer);
        }
        return gamePlayer.getId();
    }

    private int getTableNumberAfter(Integer tableNumber) {
        List<Integer> sortedTableNumbers = Stream.concat(Stream.of(tableNumber), gamePlayers.stream()
                .map(GamePlayer::getTableNumber))
                .sorted()
                .collect(Collectors.toList());

        int newTableNumberIndex = sortedTableNumbers.indexOf(tableNumber);
        if (newTableNumberIndex == sortedTableNumbers.size() - 1) {
            return sortedTableNumbers.get(0);
        } else {
            return sortedTableNumbers.get(newTableNumberIndex + 1);
        }
    }

    private void movePlayersInReferenceTo(int tableNumberAfterNewOne) {
        while (gamePlayers.getFirst().getTableNumber() != tableNumberAfterNewOne) {
            gamePlayers.addLast(gamePlayers.pollFirst());
        }
    }

    Deque<GamePlayer> getActivePlayers() {
        return gamePlayers.stream()
                .filter(GamePlayer::isActive)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    //todo
    Collection<GamePlayer> getGamePlayers() {
        return Collections.unmodifiableCollection(gamePlayers);
    }

    void updateBlinds(int small) {
        blinds.setBlinds(small);
    }

    Blinds getBlinds() {
        return blinds;
    }

    void setGameTimeStamp(long gameTimeStamp) {
        this.gameTimeStamp = gameTimeStamp;
    }

    long getGameTimeStamp() {
        return gameTimeStamp;
    }

    void setEntryFee(int entryFee) {
        this.entryFee = entryFee;
    }

    int getEntryFee() {
        return entryFee;
    }

    void rotatePlayers() {
        gamePlayers.addLast(gamePlayers.pollFirst());
    }

    boolean isFull() {
        return gamePlayers.size() == 7;
    }

    void changeActiveStatus(UUID id, boolean isActive) { //TODO think about not use boolean here
        GamePlayer gamePlayer = findPlayerById(id);
        gamePlayer.setActive(isActive);
    }

    int findEmptyTableNumber() {
        List<Integer> tableNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        gamePlayers.forEach(player -> tableNumbers.remove(player.getTableNumber()));

        if (tableNumbers.isEmpty()) {
            throw new GameIsFull();
        }
        return tableNumbers.get(0);
    }

    void removeGamePlayer(UUID id) {
        gamePlayers = gamePlayers.stream()
                .filter(player -> !player.getId().equals(id))
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    void buyIn(UUID id) {
        GamePlayer player = findPlayerById(id);
        player.buyIn();
    }

    private GamePlayer findPlayerById(UUID id) {
        return gamePlayers.stream().filter(player -> player.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new GamePlayerNotFound(id));
    }
}
