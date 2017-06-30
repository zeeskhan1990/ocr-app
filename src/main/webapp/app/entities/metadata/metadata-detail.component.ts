import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Metadata } from './metadata.model';
import { MetadataService } from './metadata.service';

@Component({
    selector: 'jhi-metadata-detail',
    templateUrl: './metadata-detail.component.html'
})
export class MetadataDetailComponent implements OnInit, OnDestroy {

    metadata: Metadata;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private metadataService: MetadataService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMetadata();
    }

    load(id) {
        this.metadataService.find(id).subscribe((metadata) => {
            this.metadata = metadata;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMetadata() {
        this.eventSubscriber = this.eventManager.subscribe(
            'metadataListModification',
            (response) => this.load(this.metadata.id)
        );
    }
}
