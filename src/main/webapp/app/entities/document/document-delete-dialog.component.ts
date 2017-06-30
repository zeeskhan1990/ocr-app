import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Document } from './document.model';
import { DocumentPopupService } from './document-popup.service';
import { DocumentService } from './document.service';

@Component({
    selector: 'jhi-document-delete-dialog',
    templateUrl: './document-delete-dialog.component.html'
})
export class DocumentDeleteDialogComponent {

    document: Document;

    constructor(
        private documentService: DocumentService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.documentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'documentListModification',
                content: 'Deleted an document'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Document is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-document-delete-popup',
    template: ''
})
export class DocumentDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private documentPopupService: DocumentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.documentPopupService
                .open(DocumentDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
