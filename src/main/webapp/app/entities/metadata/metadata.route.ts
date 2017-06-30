import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { MetadataComponent } from './metadata.component';
import { MetadataDetailComponent } from './metadata-detail.component';
import { MetadataPopupComponent } from './metadata-dialog.component';
import { MetadataDeletePopupComponent } from './metadata-delete-dialog.component';

import { Principal } from '../../shared';

export const metadataRoute: Routes = [
    {
        path: 'metadata',
        component: MetadataComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Metadata'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'metadata/:id',
        component: MetadataDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Metadata'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const metadataPopupRoute: Routes = [
    {
        path: 'metadata-new',
        component: MetadataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Metadata'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'metadata/:id/edit',
        component: MetadataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Metadata'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'metadata/:id/delete',
        component: MetadataDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Metadata'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
