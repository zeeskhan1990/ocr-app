import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { MetadataType } from './metadata-type.model';
import { MetadataTypePopupService } from './metadata-type-popup.service';
import { MetadataTypeService } from './metadata-type.service';

@Component({
    selector: 'jhi-metadata-type-delete-dialog',
    templateUrl: './metadata-type-delete-dialog.component.html'
})
export class MetadataTypeDeleteDialogComponent {

    metadataType: MetadataType;

    constructor(
        private metadataTypeService: MetadataTypeService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.metadataTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'metadataTypeListModification',
                content: 'Deleted an metadataType'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Metadata Type is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-metadata-type-delete-popup',
    template: ''
})
export class MetadataTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metadataTypePopupService: MetadataTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.metadataTypePopupService
                .open(MetadataTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
