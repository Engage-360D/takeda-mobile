//
//  Drugs.m
//  takeda
//
//  Created by Alexander Rudenko on 21.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "Drugs.h"

@interface Drugs (){
    NSMutableArray *drugs;
}

@end

@implementation Drugs

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
}

-(void)setupInterface{
    [self.goToDoctorBtn setupStandartBordered];
    self.navigationItem.rightBarButtonItems = nil;
    self.navigationItem.rightBarButtonItem = [self menuBarBtnWithImageName:@"addWhiteInCircle" selector:@selector(addPillsAction) forTarget:self];

    self.bottomText.font = [UIFont fontWithName:@"SegoeWP-Light" size:12.0f];

    self.tableView.backgroundColor = RGB(243, 243, 243);
}

-(void)initData{
    drugs = [Global recursiveMutable:@[@{@"name":@"Кругленькая", @"time":@"18:00", @"description":@"2 штуки", @"selected":@"YES"},
                                       @{@"name":@"Квадратная", @"time":@"16:20", @"description":@"5 штук", @"selected":@"YES"},
                                       @{@"name":@"Кругленькая синяя", @"time":@"13:00", @"description":@"Раздробить 2 пачки", @"selected":@"NO"},
                                       @{@"name":@"Квадратненькая синяя", @"time":@"18:45", @"description":@"целую пачку", @"selected":@"YES"},
                                       ]];
}


-(IBAction)goToTheDoctorAction:(id)sender{
    
}

-(void)addPillsAction{
    _addPills = [AddPills new];
    [self.navigationController pushViewController:_addPills animated:YES];
}

#pragma mark - Table view data source


-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    if (section == 0){
        UILabel *sectionTitle = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, tableView.width, 45)];
        sectionTitle.font = [UIFont fontWithName:@"SegoeWP" size:14];
        sectionTitle.textAlignment = NSTextAlignmentCenter;
        sectionTitle.textColor = RGB(95, 95, 95);
        sectionTitle.text = @"Сегодня";
        return sectionTitle;
    } else {
        return nil;
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return drugs.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section == 0){
        return 45;
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
    
    NSMutableDictionary *drug = drugs[indexPath.row];
    cell.cellType = ctCaptionSubtitleChecked;
    cell.caption.text = drug[@"name"];
    cell.subTitle.text = [NSString stringWithFormat:@"%@ (%@)",drug[@"time"],drug[@"description"]];
    cell.checkBtn.selected = [drug[@"selected"] boolValue];
    cell.checkBtn.info = drug;
    [cell.checkBtn addTarget:self action:@selector(setupChecked:) forControlEvents:UIControlEventTouchUpInside];
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark -

-(void)setupChecked:(buttonWithID*)sender{
    [sender.info setObject:[NSNumber numberWithBool:sender.selected] forKey:@"selected"];
}

@end
