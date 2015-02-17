//
//  CalendarPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "CalendarPage.h"

@interface CalendarPage ()

@end

@implementation CalendarPage
@synthesize emptyRecords;
@synthesize filledRecords;
@synthesize fillEmptySwitch;
@synthesize state;

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
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
}

-(void)setupInterface{
    self.fillEmptySwitch.clipsToBounds = YES;
    self.fillEmptySwitch.layer.cornerRadius = 4.0f;
    self.tableView.backgroundColor = RGB(243, 243, 243);
    self.navigationItem.rightBarButtonItems = nil;
    self.navigationItem.rightBarButtonItems = @[[self menuBarBtnWithImageName:@"addWhiteInCircle" selector:@selector(addPillsAction) forTarget:self],[self alarmButton]];

}

-(void)addPillsAction{
    _addPills = [AddPills new];
    [self.navigationController pushViewController:_addPills animated:YES];
}


-(IBAction)selectViewType:(UISegmentedControl*)sender{
    [self selectView:sender.selectedSegmentIndex];
}

-(void)selectView:(int)index{
    switch (index) {
        case 0:{

            break;
        }
        case 1:{

            break;
        }
    }
}


-(void)initData{

}

-(void)showInfo{

}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    switch (state) {
        case dNew:
            return emptyRecords.count;

        case dFilled:
            return filledRecords.count;

    }
    return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    switch (state) {
        case dNew:
            return [emptyRecords[section] count];
            
        case dFilled:
            return [filledRecords[section] count];
            
    }
    return 0;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section == 0){
        return 0;
    } else {
        return 75.f;
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
    
    NSMutableDictionary *menu ;//= menu_data[indexPath.section][indexPath.row];
    cell.cellType = [menu[@"type"] intValue];
    cell.caption.text = menu[@"title"];
    cell.subTitle.text = menu[@"subtitle"];
    cell.rightCaption.text = menu[@"rightTitle"];
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    NSMutableDictionary *item = menu_data[indexPath.section][indexPath.row];
//    switch ([item[@"action"] intValue]) {
//            
//        case physExercises:{
//            
//            break;
//        }
//        case haveDrugs:{
//            [self goToDrugs];
//            break;
//        }
//        case eatToday:{
//            
//            break;
//        }
//        case myRecommendations:{
//            [self openResultsAnal];
//            break;
//        }
//        case mySuccess:{
//            
//            break;
//        }
//        case commonReport:{
//            [self openSiteReport];
//            break;
//        }
//            
//            
//    }
    
    
    [self.tableView reloadData];
}




@end
