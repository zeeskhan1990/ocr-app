import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Metadata } from './metadata.model';
import { MetadataPopupService } from './metadata-popup.service';
import { MetadataService } from './metadata.service';
import { MetadataType, MetadataTypeService } from '../metadata-type';
import { Document, DocumentService } from '../document';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-metadata-dialog',
    templateUrl: './metadata-dialog.component.html'
})
export class MetadataDialogComponent implements OnInit {

    metadata: Metadata;
    authorities: any[];
    isSaving: boolean;

    metadatatypes: MetadataType[];

    documents: Document[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private metadataService: MetadataService,
        private metadataTypeService: MetadataTypeService,
        private documentService: DocumentService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.metadataTypeService
            .query({filter: 'metadata-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.metadata.metadataType || !this.metadata.metadataType.id) {
                    this.metadatatypes = res.json;
                } else {
                    this.metadataTypeService
                        .find(this.metadata.metadataType.id)
                        .subscribe((subRes: MetadataType) => {
                            this.metadatatypes = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.documentService.query()
            .subscribe((res: ResponseWrapper) => { this.documents = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.metadata.id !== undefined) {
            this.subscribeToSaveResponse(
                this.metadataService.update(this.metadata), false);
        } else {
            this.subscribeToSaveResponse(
                this.metadataService.create(this.metadata), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Metadata>, isCreated: boolean) {
        result.subscribe((res: Metadata) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Metadata, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Metadata is created with identifier ${result.id}`
            : `A Metadata is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'metadataListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackMetadataTypeById(index: number, item: MetadataType) {
        return item.id;
    }

    trackDocumentById(index: number, item: Document) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-metadata-popup',
    template: ''
})
export class MetadataPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metadataPopupService: MetadataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.metadataPopupService
                    .open(MetadataDialogComponent, params['id']);
            } else {
                this.modalRef = this.metadataPopupService
                    .open(MetadataDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
