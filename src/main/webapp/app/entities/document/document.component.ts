import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService, DataUtils } from 'ng-jhipster';

import { Document } from './document.model';
import { DocumentService } from './document.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-document',
    templateUrl: './document.component.html'
})
export class DocumentComponent implements OnInit, OnDestroy {
documents: Document[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private documentService: DocumentService,
        private alertService: AlertService,
        private dataUtils: DataUtils,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.documentService.query().subscribe(
            (res: ResponseWrapper) => {
                this.documents = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInDocuments();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Document) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInDocuments() {
        this.eventSubscriber = this.eventManager.subscribe('documentListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
