import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { MetadataType } from './metadata-type.model';
import { MetadataTypeService } from './metadata-type.service';

@Component({
    selector: 'jhi-metadata-type-detail',
    templateUrl: './metadata-type-detail.component.html'
})
export class MetadataTypeDetailComponent implements OnInit, OnDestroy {

    metadataType: MetadataType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private metadataTypeService: MetadataTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMetadataTypes();
    }

    load(id) {
        this.metadataTypeService.find(id).subscribe((metadataType) => {
            this.metadataType = metadataType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMetadataTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'metadataTypeListModification',
            (response) => this.load(this.metadataType.id)
        );
    }
}
