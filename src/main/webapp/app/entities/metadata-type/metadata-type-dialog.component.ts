import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { MetadataType } from './metadata-type.model';
import { MetadataTypePopupService } from './metadata-type-popup.service';
import { MetadataTypeService } from './metadata-type.service';

@Component({
    selector: 'jhi-metadata-type-dialog',
    templateUrl: './metadata-type-dialog.component.html'
})
export class MetadataTypeDialogComponent implements OnInit {

    metadataType: MetadataType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private metadataTypeService: MetadataTypeService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.metadataType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.metadataTypeService.update(this.metadataType), false);
        } else {
            this.subscribeToSaveResponse(
                this.metadataTypeService.create(this.metadataType), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<MetadataType>, isCreated: boolean) {
        result.subscribe((res: MetadataType) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: MetadataType, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Metadata Type is created with identifier ${result.id}`
            : `A Metadata Type is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'metadataTypeListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-metadata-type-popup',
    template: ''
})
export class MetadataTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metadataTypePopupService: MetadataTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.metadataTypePopupService
                    .open(MetadataTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.metadataTypePopupService
                    .open(MetadataTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
