import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { MetadataTypeComponent } from './metadata-type.component';
import { MetadataTypeDetailComponent } from './metadata-type-detail.component';
import { MetadataTypePopupComponent } from './metadata-type-dialog.component';
import { MetadataTypeDeletePopupComponent } from './metadata-type-delete-dialog.component';

import { Principal } from '../../shared';

export const metadataTypeRoute: Routes = [
    {
        path: 'metadata-type',
        component: MetadataTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MetadataTypes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'metadata-type/:id',
        component: MetadataTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MetadataTypes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const metadataTypePopupRoute: Routes = [
    {
        path: 'metadata-type-new',
        component: MetadataTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MetadataTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'metadata-type/:id/edit',
        component: MetadataTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MetadataTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'metadata-type/:id/delete',
        component: MetadataTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MetadataTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
