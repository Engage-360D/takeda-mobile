//
//  SearchInstitutionPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "SearchInstitutionPage.h"

@interface SearchInstitutionPage (){
//    GMSMapView *mapView_;
    NSMutableArray *lpuList;
}

@end

@implementation SearchInstitutionPage
@synthesize mapView_;
@synthesize lpuDetail;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setupInterface];
    [self drawMap];
    [self initData];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.container.clipsToBounds = YES;
    [self selectView:self.listMapSwitch.selectedSegmentIndex];
}

-(void)setupInterface{
    self.listMapSwitch.clipsToBounds = YES;
    self.listMapSwitch.layer.cornerRadius = 4.0f;
}

-(void)initData{
    // name
    // adres
    // location
    // category
    // short description
    
    lpuList = [NSMutableArray arrayWithArray:@[@{@"name":@"Учереждение N1", @"address":@"Улица Такая то, дом 777", @"location":[[CLLocation alloc] initWithLatitude:55.826813 longitude:37.561770], @"category":@"Профилактика", @"descr":@"больница высшей категории"},
                                               @{@"name":@"Учереждение N1", @"address":@"Улица Такая то, дом 777", @"location":[[CLLocation alloc] initWithLatitude:55.789769 longitude:37.710773], @"category":@"Профилактика", @"descr":@"больница высшей категории"},
                                               @{@"name":@"Учереждение N1", @"address":@"Улица Такая то, дом 777", @"location":[[CLLocation alloc] initWithLatitude:55.759259 longitude:37.565204], @"category":@"Профилактика", @"descr":@"больница высшей категории"},
                                               @{@"name":@"Учереждение N1", @"address":@"Улица Такая то, дом 777", @"location":[[CLLocation alloc] initWithLatitude:55.697199 longitude:37.546664], @"category":@"Профилактика", @"descr":@"больница высшей категории"},
                                               @{@"name":@"Учереждение N1", @"address":@"Улица Такая то, дом 777", @"location":[[CLLocation alloc] initWithLatitude:55.747280 longitude:37.624942], @"category":@"Профилактика", @"descr":@"больница высшей категории"},

                                               ]];
}



-(IBAction)selectViewType:(UISegmentedControl*)sender{
    [self selectView:sender.selectedSegmentIndex];
}

-(void)selectView:(int)index{
    switch (index) {
        case 0:{
            self.mapContainer.hidden = NO;
            self.tableContainer.hidden = YES;
            [self drawMarkers];
            break;
        }
        case 1:{
            self.mapContainer.hidden = YES;
            self.tableContainer.hidden = NO;
            [self.tableView reloadData];
            break;
        }
    }
}

#pragma mark - Map

-(void)drawMap{

    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:-33.86
                                                            longitude:151.20
                                                                   zoom:6];
    
   // mapView_ = [[GMSMapView alloc] initWithFrame:self.container.bounds];
    mapView_.settings.myLocationButton = YES;
   // mapView_.camera = camera;
    mapView_.delegate = self;

    mapView_.myLocationEnabled = YES;
    //self.mapContainer = mapView_;
    [self.mapContainer addSubview:mapView_];

    

}

-(void)drawMarkers{
    for (NSMutableDictionary* place in lpuList){
        CLLocation *loc = place[@"location"];
        GMSMarker *marker = [[GMSMarker alloc] init];
        marker.position = CLLocationCoordinate2DMake(loc.coordinate.latitude, loc.coordinate.longitude);
        marker.title = place[@"category"];
        marker.snippet = place[@"descr"];
        marker.map = mapView_;
        
    }
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return lpuList.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section == 0){
        return 50;
    } else {
        return 0.f;
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"StandartCombyCell";
    
    StandartCombyCell *cell = (StandartCombyCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"StandartCombyCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[StandartCombyCell class]])
            {
                cell = (StandartCombyCell *)currentObject;
                break;
            }
        }
    }
    
    NSMutableDictionary *menu = lpuList[indexPath.row];
    cell.cellType = ctCaptionSubtitleRightArrow;
    cell.caption.text = menu[@"name"];
    cell.subTitle.text = menu[@"address"];
    
    cell.caption.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];

    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    [self openLpu:lpuList[indexPath.row]];
}

-(void)openLpu:(NSMutableDictionary*)lpu{
    if (!lpuDetail){
        lpuDetail = [LPUDetail new];
    }
    lpuDetail.lpu = lpu;
    [self.navigationController pushViewController:lpuDetail animated:YES];
}


@end
