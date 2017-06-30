import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { MetadataType } from './metadata-type.model';
import { MetadataTypeService } from './metadata-type.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-metadata-type',
    templateUrl: './metadata-type.component.html'
})
export class MetadataTypeComponent implements OnInit, OnDestroy {
metadataTypes: MetadataType[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private metadataTypeService: MetadataTypeService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.metadataTypeService.query().subscribe(
            (res: ResponseWrapper) => {
                this.metadataTypes = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInMetadataTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: MetadataType) {
        return item.id;
    }
    registerChangeInMetadataTypes() {
        this.eventSubscriber = this.eventManager.subscribe('metadataTypeListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
