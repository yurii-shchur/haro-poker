import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {HelloService} from "../../api/hello.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {LoginModalComponent} from "../login-modal/login-modal.component";
import {PlayerService} from "../../api/player.service";
import {Player} from "../../model/player";
import {CardsService} from "../../api/cards.service";

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})

export class TableComponent implements OnInit, AfterViewInit, OnDestroy {

  greeting: string;
  helloSubscription: Subscription;
  playersSubscription: Subscription;
  players: Player[];
  cards$: Observable<any>;
  playerNames = [];

  constructor(private helloService: HelloService, private modalService: NgbModal,
              private playerService: PlayerService, private cardsService: CardsService) {
  }

  ngOnInit(): void {
    localStorage.setItem("playerId", '1');

    this.cards$ = this.cardsService.getCards();
    this.helloSubscription = this.helloService.getGreeting().subscribe(result => this.greeting = result.content);
    this.playersSubscription = this.playerService.getPlayers().subscribe(players => this.players = players);
  }

  ngAfterViewInit() {
    this.openModal();
  }

  openModal() {
    this.modalService.open(LoginModalComponent, {
      backdrop: 'static',
      keyboard: false
    }).result.then((playerName) => {
      this.playerNames.push(playerName)
    });
  }

  getPlayerById(id: number) {
    return this.players.find(player => player.id == id);
  }

  ngOnDestroy(): void {
    this.helloSubscription.unsubscribe();
    this.playersSubscription.unsubscribe();
  }
}
