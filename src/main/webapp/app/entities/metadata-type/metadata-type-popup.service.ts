import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MetadataType } from './metadata-type.model';
import { MetadataTypeService } from './metadata-type.service';

@Injectable()
export class MetadataTypePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private metadataTypeService: MetadataTypeService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.metadataTypeService.find(id).subscribe((metadataType) => {
                this.metadataTypeModalRef(component, metadataType);
            });
        } else {
            return this.metadataTypeModalRef(component, new MetadataType());
        }
    }

    metadataTypeModalRef(component: Component, metadataType: MetadataType): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.metadataType = metadataType;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
