import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { Metadata } from './metadata.model';
import { MetadataService } from './metadata.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-metadata',
    templateUrl: './metadata.component.html'
})
export class MetadataComponent implements OnInit, OnDestroy {
metadata: Metadata[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private metadataService: MetadataService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.metadataService.query().subscribe(
            (res: ResponseWrapper) => {
                this.metadata = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInMetadata();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Metadata) {
        return item.id;
    }
    registerChangeInMetadata() {
        this.eventSubscriber = this.eventManager.subscribe('metadataListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
