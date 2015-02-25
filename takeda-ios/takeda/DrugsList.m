//
//  DrugsList.m
//  takeda
//
//  Created by Alexander Rudenko on 25.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "DrugsList.h"

@interface DrugsList ()

@end

@implementation DrugsList
@synthesize drugs;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
}

-(void)setupInterface{
    self.navigationItem.rightBarButtonItems = nil;
    self.navigationItem.rightBarButtonItems = @[[self menuBarBtnWithImageName:@"addWhiteInCircle" selector:@selector(addPillsAction) forTarget:self],[self alarmButton]];
    self.tableView.tableFooterView = self.tableView.separ;
    
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
}

-(void)initData{
    [GlobalData loadPillsCompletition:^(BOOL success, id result){
        drugs = [GlobalData pills];
        [self.tableView reloadData];
    }];
}


#pragma mark - Table view data source


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return drugs.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
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
    cell.cellType = ctCaptionSubtitleRightArrow;
    cell.caption.text = drug[@"name"];
    
    NSDate *dTime = [Global parseTime:drug[@"time"]];
    NSString *sbt = [Global dictionaryWithValue:drug[@"repeat"] ForKey:@"name" InArray:[AddPills expArray]][@"title"];
    cell.subTitle.text = [NSString stringWithFormat:@"%@ ( %@ шт. %@)",[dTime stringWithFormat:@"HH:mm"],drug[@"quantity"], sbt];

    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    [self goToDrug:drugs[indexPath.row]];
}

-(void)goToDrug:(NSMutableDictionary*)drug{
    _addPills = [AddPills new];
    _addPills.drug = drug;
    _addPills.readOnly = YES;
    [self.navigationController pushViewController:_addPills animated:YES];
}

-(void)addPillsAction{
    _addPills = [AddPills new];
    [self.navigationController pushViewController:_addPills animated:YES];
}

@end
