package app.domain.game


import app.domain.player.GamePlayer
import spock.lang.Shared
import spock.lang.Specification

class GameTest extends Specification {

    @Shared
            gamePlayer1 = new GamePlayer("Piotr1", 1)
    @Shared
            gamePlayer2 = new GamePlayer("Piotr2", 2)
    @Shared
            gamePlayer3 = new GamePlayer("Piotr3", 3)
    @Shared
            gamePlayer4 = new GamePlayer("Piotr4", 4)
    @Shared
            gamePlayer5 = new GamePlayer("Piotr5", 5)
    @Shared
            gamePlayer6 = new GamePlayer("Piotr6", 6)
    @Shared
            gamePlayer7 = new GamePlayer("Piotr7", 7)


    def "should correctly add player to the game"() {
        given:
        def game = [gamePlayers: gamePlayers as ArrayDeque<GamePlayer>] as Game

        when:
        game.addPlayer(newPlayer)

        then:
        game.gamePlayers as ArrayList == gameAfter as ArrayList

        where:
        gamePlayers                                          | newPlayer   | gameAfter
        [gamePlayer2, gamePlayer3]                           | gamePlayer1 | [gamePlayer2, gamePlayer3, gamePlayer1]
        [gamePlayer1, gamePlayer3]                           | gamePlayer2 | [gamePlayer1, gamePlayer2, gamePlayer3]
        []                                                   | gamePlayer1 | [gamePlayer1]
        [gamePlayer4, gamePlayer5, gamePlayer6]              | gamePlayer3 | [gamePlayer4, gamePlayer5,
                                                                              gamePlayer6, gamePlayer3]
        [gamePlayer6, gamePlayer7, gamePlayer1, gamePlayer2] | gamePlayer3 | [gamePlayer6, gamePlayer7,
                                                                              gamePlayer1, gamePlayer2, gamePlayer3]
        [gamePlayer6, gamePlayer1, gamePlayer2, gamePlayer3,
         gamePlayer4, gamePlayer5]                           | gamePlayer7 | [gamePlayer6, gamePlayer7, gamePlayer1,
                                                                              gamePlayer2, gamePlayer3, gamePlayer4,
                                                                              gamePlayer5]
    }
}
