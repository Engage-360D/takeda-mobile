//
//  SearchInstitutionPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GoogleMaps/GoogleMaps.h>
#import <CoreLocation/CoreLocation.h>
#import "StandartCombyCell.h"
#import "LPUDetail.h"
#import "UITextFieldAutocompl.h"
#import "GClusterManager.h"
#import "EGORefreshTableHeaderView.h"
#import "MDDirectionService.h"

@import CoreLocation;



@interface SearchInstitutionPage : VControllerExt <GMSMapViewDelegate, EGORefreshTableHeaderDelegate, CLLocationManagerDelegate>

@property (nonatomic, strong) IBOutlet UISegmentedControl *listMapSwitch;
@property (nonatomic, strong) IBOutlet UIView *container;
@property (nonatomic, strong) IBOutlet UIView *mapContainer;
@property (nonatomic, strong) IBOutlet UIView *searchContainer;
@property (nonatomic, strong) IBOutlet UIView *tableContainer;
@property (nonatomic, strong) IBOutlet UIView *searchFiedsContainer;
@property (nonatomic, strong) IBOutlet UIView *filtrFieldsContainer;


@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) IBOutlet UITextFieldAutocompl *searchCity;
@property (nonatomic, strong) IBOutlet UITextFieldAutocompl *searchSpec;
@property (nonatomic, strong) IBOutlet UITextFieldAutocompl *filtrSpec;
@property (nonatomic, strong) IBOutlet UIButton *searchBtn;
@property (nonatomic, strong) IBOutlet UIButton *showHideSearchBtn;
@property (nonatomic, strong) IBOutlet UIButton *normZoomBtn;
@property (nonatomic, strong) IBOutlet UIButton *drawRouteBtn;

@property (nonatomic, strong) IBOutlet UISlider *zoomSlider;

@property (nonatomic, strong) IBOutlet GMSMapView *mapView_;
@property (nonatomic, strong) LPUDetail *lpuDetail;
@property (nonatomic, strong) NSMutableArray *LPUArray;
@property (nonatomic, strong) NSArray *lpuList;
@property (nonatomic, strong) NSMutableDictionary *selectedLPU;

@property (nonatomic, strong) EGORefreshTableHeaderView *refreshHeaderView;

@property (nonatomic, strong) CLLocationManager *locMan;

@end
